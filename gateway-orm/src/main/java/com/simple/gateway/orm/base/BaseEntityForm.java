package com.simple.gateway.orm.base;

import com.simple.gateway.common.exception.BadRequestException;
import com.simple.gateway.common.exception.BaseException;
import com.simple.gateway.common.exception.NotFoundException;
import com.simple.gateway.common.util.SpringContextUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public abstract class BaseEntityForm<E extends BaseEntity> {

    protected Long id;

    //是否为编辑模式。在preCheck中可以快速的知道当前所处的模式。
    protected boolean createMode = false;

    private Class<E> clazz;


    public BaseEntityForm(Class<E> clazz) {
        this.clazz = clazz;
    }



    protected abstract void preCheck(E entity);


    public E create() {
        this.createMode = true;

        E entity;
        try {
            entity = clazz.newInstance();
        } catch (Exception e) {
            throw new BaseException("构建实体时出错！");
        }

        this.preCheck(entity);

        return entity;
    }


    public E update() {
        this.createMode = false;

        if (id == null) {
            throw new BadRequestException("编辑时id必填！");
        }

        E entity = this.check(this.id);

        this.preCheck(entity);

        entity.setUpdatedAt(new Date());

        return entity;
    }

    /**
     * 从数据库中检出一个当前泛型的实例
     * 找不到抛异常
     */
    public E check(long id) {
        E e = this.find(id);

        if (e == null) {
            throw new NotFoundException("id={}对应的{}不存在", id, this.clazz.getSimpleName());
        }

        return e;
    }

    /**
     * 从数据库中找出一个当前泛型的实例
     * 找不到返回null
     */
    public E find(long id) {
        return this.getMapper().selectById(id);
    }

    /**
     * 获取到 Entity 对应的 Mapper
     */
    @SuppressWarnings("unchecked")
    protected BaseMapper<E> getMapper() {
        return (BaseMapper<E>) SpringContextUtil.getGenericBean(BaseMapper.class, this.clazz);
    }

}
