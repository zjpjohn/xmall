package cn.exrick.content.service.impl;

import cn.exrick.common.exception.XmallException;
import cn.exrick.common.pojo.ZTreeNode;
import cn.exrick.content.service.ContentCatService;
import cn.exrick.dto.ContentCatDto;
import cn.exrick.dto.DtoUtil;
import cn.exrick.mapper.TbContentCategoryMapper;
import cn.exrick.pojo.TbContentCategory;
import cn.exrick.pojo.TbContentCategoryExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCatServiceImpl implements ContentCatService {

    private final static Logger log= LoggerFactory.getLogger(ContentCatServiceImpl.class);

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public TbContentCategory getContentCatById(Long id) {

        TbContentCategory tbContentCategory=tbContentCategoryMapper.selectByPrimaryKey(id);
        if(tbContentCategory==null){
            throw new XmallException("通过id获得内容分类失败");
        }
        return tbContentCategory;
    }

    @Override
    public List<ZTreeNode> getContentCatList(Long parentId) {

        TbContentCategoryExample example=new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria=example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        example.setOrderByClause("sort_order");
        List<TbContentCategory> catList=tbContentCategoryMapper.selectByExample(example);

        List<ZTreeNode> list=new ArrayList<>();

        for(int i=0;i<catList.size();i++){
            ZTreeNode zTreeNode= DtoUtil.TbContentCategory2ZTreeNode(catList.get(i));
            list.add(zTreeNode);
        }

        return list;
    }

    @Override
    public int addContentCat(ContentCatDto contentCatDto) {

        TbContentCategory tbContentCategory=DtoUtil.ContentCatDto2TbContentCategory(contentCatDto);
        tbContentCategory.setParentId((long) 0);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());

        if(tbContentCategoryMapper.insert(tbContentCategory)!=1){
            throw new XmallException("添加内容分类失败");
        }
        return 1;
    }

    @Override
    public int updateContentCat(ContentCatDto contentCatDto) {

        TbContentCategory tbContentCategory=DtoUtil.ContentCatDto2TbContentCategory(contentCatDto);

        TbContentCategory old=getContentCatById(tbContentCategory.getId());
        tbContentCategory.setParentId(old.getParentId());
        tbContentCategory.setIcon(old.getIcon());
        tbContentCategory.setCreated(old.getCreated());
        tbContentCategory.setUpdated(new Date());

        if(tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory)!=1){
            throw new XmallException("更新内容分类失败");
        }
        return 1;
    }

    @Override
    public int deleteContentCat(Long id) {

        if(tbContentCategoryMapper.deleteByPrimaryKey(id)!=1){
            throw new XmallException("删除内容分类失败");
        }
        return 1;
    }
}