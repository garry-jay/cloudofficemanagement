package com.cloud.server.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author liangzijie
 * @since 2024-10-21
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
//of = "name"：指定仅用 name 字段来生成 equals() 和 hashCode() 方法。它表明该注解只关注对象中 name 字段的相等性和哈希值，而忽略其他字段
@EqualsAndHashCode(callSuper = false,of = "name")
@ApiModel(value="Nation对象", description="")
public class Nation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "民族")
    @Excel(name="民族")
    @NonNull //在进行有参构造时，name必须要填
    private String name;


}
