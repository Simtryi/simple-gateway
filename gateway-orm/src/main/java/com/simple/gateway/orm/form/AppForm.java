package com.simple.gateway.orm.form;

import com.simple.gateway.common.exception.BadRequestException;
import com.simple.gateway.common.util.SpringContextUtil;
import com.simple.gateway.common.util.StringUtil;
import com.simple.gateway.orm.base.BaseEntityForm;
import com.simple.gateway.orm.entity.app.App;
import com.simple.gateway.orm.entity.app.enums.AppStatus;
import com.simple.gateway.orm.mapper.AppMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;


@Data
@EqualsAndHashCode(callSuper = true)
public class AppForm extends BaseEntityForm<App> {

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用code，全局唯一
     */
    private String code;

    /**
     * 应用描述
     */
    private String description;

    /**
     * 应用状态
     */
    private AppStatus status;

    /**
     * 负责人花名
     */
    private String ownerNickname;

    /**
     * 负责人工号
     */
    private String ownerNo;

    /**
     * 更新人花名
     */
    private String updatedOwnerNickname;

    /**
     * 更新人工号
     */
    private String updatedOwnerNo;


    public AppForm() {
        super(App.class);
    }

    /**
     * 在这里将 form 中的属性赋值给 App，并检查参数合理性。
     */
    protected void preCheck(@NonNull App app) {

        //code 全局唯一校验:
        //1.创建时验证 2.编辑且修改时验证
        AppMapper appMapper = SpringContextUtil.getBean(AppMapper.class);
        if(this.createMode || (!StringUtil.equals(app.getCode(), code))) {
            App dbApp = appMapper.selectByCode(code);

            if(dbApp != null) {
                throw new BadRequestException("{} 已经存在，请使用其他code", code);
            }
        }

        app.setName(name);
        app.setCode(code);
        app.setDescription(description);
        app.setStatus(status);
        app.setUpdatedOwnerNickname(updatedOwnerNickname);
        app.setUpdatedOwnerNo(updatedOwnerNo);

        if (createMode) {
            app.setOwnerNickname(ownerNickname);
            app.setOwnerNo(ownerNo);
        }

    }

}
