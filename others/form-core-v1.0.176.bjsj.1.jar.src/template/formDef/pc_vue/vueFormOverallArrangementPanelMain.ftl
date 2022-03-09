<#if group.hasTitle()>
<div class="panel panel-default">
<div class="panel-heading">${group.comment} </div>
</#if>
<div class="panel-body">
 ${One2OneChildsOne2ManyRelations(group)}
	<#list group.columnList as groupColumn>
	<div class="col-xs-${12/groupColumn.row} form-item" v-ab-permission:show="permission.${relation.busObj.key}.${groupColumn.tableKey}.${groupColumn.key}">
        <label class="form-item-label">${groupColumn.comment}</label>
        <div class="form-item-content"> ${vueGenerator.getColumn(group,groupColumn)} </div>
    </div>
	</#list>
</div>
<#if group.hasTitle()>
</div>
</#if>
<#function One2OneChildsOne2ManyRelations group> 
	<#assign relationList = group.getOne2OneChildsOne2ManyRelations()>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
		 <div class="col-xs-12">
		<#list relationList as relation>
            <ab-sub-detail class="btn btn-link btn-sm fa fa-detail"  v-model="${vueGenerator.getScopePath(relation.parent)}<#if relation.parent.type =='oneToMany'>[index]</#if>" dialog-id="${relation.getBusObj().getKey()}-${relation.tableKey}"  v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}" >${relation.tableComment}详情</ab-sub-detail>
		</#list>
		</div>
		</#if>
	</#assign>
	<#return rtn>
</#function>

 