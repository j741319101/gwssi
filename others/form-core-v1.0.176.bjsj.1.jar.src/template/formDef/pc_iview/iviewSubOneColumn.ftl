<<#if iviewGenerator.isMultilayer(relation)>Modal ${iviewGenerator.getDivVIf(relation)} width="800"<#else>Card</#if> ${iviewGenerator.getSubAttrs(relation)} v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}" >
	<div <#if iviewGenerator.isMultilayer(relation)>slot="header"<#else>slot="title"</#if> ><span class="title">${relation.tableComment}</span>
	 	<ab-sub-add class="ivu-btn ivu-btn-primary" v-model="${iviewGenerator.getScopePath(relation)}" v-bind:init-data="initData.${relation.busObj.key}.${relation.tableKey}" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}">添加 </ab-sub-add>
	</div>
	<ab-sub-scope v-slot="scope" :permissiona="permission" :table-permissiona="tablePermission" v-for="(${relation.tableKey},index) in ${iviewGenerator.getScopePath(relation)}"> ${getOne2ManyChild(relation)}
 	<ab-sub-del  class="ivu-btn ivu-btn-error" v-model="${iviewGenerator.getScopePath(relation)}" v-bind:index="index" v-ab-permission:edit="scope.tablePermission.${relation.busObj.key}.${relation.tableKey}">移除</ab-sub-del>
		<table class="form-table">
		<#list relation.table.columnsWithOutHidden as column>
			<tr>
				<th>${column.comment}</th>
				<td>${iviewGenerator.getColumn(column,relation)} </td>
			</tr>
		</#list>
		</table>
		 ${getOne2OneChild(relation)}
	</ab-sub-scope>
</<#if iviewGenerator.isMultilayer(relation)>Modal<#else>Card</#if>>

<#function getOne2OneChild relation> 
	<#assign relationList = relation.getChildren('oneToOne')>
	<#assign rtn>
		<#list relationList as relation>
			<Card ${iviewGenerator.getSubAttrs(relation)} >
				<div  slot="title"> <span class="title">${relation.tableComment}</span>
					${getOne2ManyChild(relation)}
				</div>
				<table class="form-table">
					<#list relation.table.columnsWithOutHidden as column>
						<tr>
							<th>${column.comment}</th>
							<td>${iviewGenerator.getColumn(column,relation)} </td>
						</tr>
					</#list>
				</table>
				${getOne2OneChild(relation)}
			</Card>
		</#list>
	</#assign>
	<#return rtn>
</#function>

<#function getOne2ManyChild relation> 
	<#assign relationList = relation.getChildren('oneToMany')>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
		<#list relationList as relation>
			<ab-sub-detail v-bind:sub-temp-data.sync="subTempData" pkey="${relation.parent.tableKey}" class="ivu-btn ivu-btn-default"  v-model="${iviewGenerator.getScopePath(relation.parent)}<#if relation.parent.type =='oneToMany'>[index]</#if>" table-key="${relation.getBusObj().getKey()}_${relation.tableKey}" v-ab-permission:show="scope.tablePermission.${relation.busObj.key}.${relation.tableKey}" >${relation.tableComment}详情</ab-sub-detail>
		</#list>
		</#if>
	</#assign>
	<#return rtn>
</#function>