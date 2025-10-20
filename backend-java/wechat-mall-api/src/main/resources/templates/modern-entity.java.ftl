package ${package.Entity};

<#list table.importPackages as pkg>
import ${pkg};
</#list>
<#if swagger>
import io.swagger.v3.oas.annotations.media.Schema;
</#if>
<#if entityLombokModel>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
</#if>
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * ${table.comment!}实体类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
  <#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
  <#else>
@EqualsAndHashCode(callSuper = false)
  </#if>
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
</#if>
<#if table.convert>
@TableName("${schemaName}${table.name}")
</#if>
<#if swagger>
@Schema(name = "${entity}", description = "${table.comment!}实体")
</#if>
<#if superEntityClass??>
public class ${entity} extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
<#elseif activeRecord>
public class ${entity} extends Model<${entity}> {
<#else>
public class ${entity} implements Serializable {
</#if>
<#if entitySerialVersionUID>

    private static final long serialVersionUID = 1L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------- -->
<#list table.fields as field>

<#if field.keyFlag>
<#assign keyPropertyName="${field.propertyName}"/>
</#if>
<#if field.comment!?length gt 0>
  <#if swagger>
    <#if field.keyFlag>
    @Schema(description = "${field.comment}", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    <#elseif field.propertyName == "createdAt" || field.propertyName == "updatedAt">
    @Schema(description = "${field.comment}", example = "2024-01-01T12:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    <#elseif field.propertyType == "String">
    @Schema(description = "${field.comment}", example = "示例值")
    <#elseif field.propertyType == "Integer">
    @Schema(description = "${field.comment}", example = "1")
    <#elseif field.propertyType == "Long">
    @Schema(description = "${field.comment}", example = "1", format = "int64")
    <#elseif field.propertyType == "BigDecimal">
    @Schema(description = "${field.comment}", example = "99.99", type = "number", format = "decimal")
    <#elseif field.propertyType == "Boolean">
    @Schema(description = "${field.comment}", example = "true")
    <#else>
    @Schema(description = "${field.comment}")
    </#if>
  <#else>
    /**
     * ${field.comment}
     */
  </#if>
</#if>
<#if field.keyFlag>
<#-- 主键 -->
  <#if field.keyIdentityFlag>
    @TableId(value = "${field.annotationColumnName}", type = IdType.AUTO)
  <#elseif idType??>
    @TableId(value = "${field.annotationColumnName}", type = IdType.${idType})
  <#elseif field.convert>
    @TableId("${field.annotationColumnName}")
  </#if>
<#-- 普通字段 -->
<#elseif field.fill??>
<#-- -----   存在字段填充设置   ----- -->
  <#if field.convert>
    @TableField(value = "${field.annotationColumnName}", fill = FieldFill.${field.fill})
  <#else>
    @TableField(fill = FieldFill.${field.fill})
  </#if>
<#elseif field.convert>
    @TableField("${field.annotationColumnName}")
</#if>
<#-- 乐观锁注解 -->
<#if field.versionField>
    @Version
</#if>
<#-- 逻辑删除注解 -->
<#if field.logicDeleteField>
    @TableLogic
    @JsonIgnore
</#if>
<#-- 时间字段格式化 -->
<#if field.propertyType == "LocalDateTime">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
<#elseif field.propertyType == "LocalDate">
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
<#elseif field.propertyType == "LocalTime">
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
</#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->
<#if !entityLombokModel>
<#list table.fields as field>
  <#if field.propertyType == "boolean">
    <#assign getprefix="is"/>
  <#else>
    <#assign getprefix="get"/>
  </#if>

    public ${field.propertyType} ${getprefix}${field.capitalName}() {
        return ${field.propertyName};
    }

  <#if chainModel>
    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
  <#else>
    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
  </#if>
        this.${field.propertyName} = ${field.propertyName};
  <#if chainModel>
        return this;
  </#if>
    }
</#list>
</#if>
<#if entityColumnConstant>
  <#list table.fields as field>

    public static final String ${field.name?upper_case} = "${field.name}";
  </#list>
</#if>
<#if activeRecord>

    @Override
    public Serializable pkVal() {
  <#if keyPropertyName??>
        return this.${keyPropertyName};
  <#else>
        return null;
  </#if>
    }
</#if>
<#if !entityLombokModel>

    @Override
    public String toString() {
        return "${entity}{" +
  <#list table.fields as field>
    <#if field_index==0>
        "${field.propertyName}=" + ${field.propertyName} +
    <#else>
        ", ${field.propertyName}=" + ${field.propertyName} +
    </#if>
  </#list>
        "}";
    }
</#if>
}