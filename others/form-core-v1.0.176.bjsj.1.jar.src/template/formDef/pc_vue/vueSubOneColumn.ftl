<div ${vueGenerator.getSubAttrs(relation)} v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}" >
	<div ${vueGenerator.getDivVIf(relation)}>
		<div class="ibox-title"><span class="title">${relation.tableComment}</span>
		 	<ab-sub-add href="javascript:void(0)" class="btn btn-primary btn-sm fa fa-plus" v-model="${vueGenerator.getScopePath(relation)}" v-bind:init-data="initData.${relation.busObj.key}.${relation.tableKey}" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}">添加 </ab-sub-add>
		</div>
		<ab-sub-scope v-slot="scope" :permissiona="permission" :table-permissiona="tablePermission" v-for="(${relation.tableKey},index) in ${vueGenerator.getScopePath(relation)}"> ${getOne2ManyChild(relation)}
	 	<ab-sub-del href="javascript:void(0);" v-model="${vueGenerator.getScopePath(relation)}" v-bind:index="index" class="btn btn-danger btn-xs fa fa-delete pull-right" v-ab-permission:edit="scope.tablePermission.${relation.busObj.key}.${relation.tableKey}">移除</ab-sub-del>	
			<table class="form-table">
			<#list relation.table.columnsWithOutHidden as column>
				<tr>
					<th>${column.comment}</th>
					<td>${vueGenerator.getColumn(column,relation)} </td>
				</tr>
			</#list>
			</table>
			 ${getOne2OneChild(relation)}
		</ab-sub-scope>
	</div>
</div>

<#function getOne2OneChild relation> 
	<#assign relationList = relation.getChildren('oneToOne')>
	<#assign rtn>
		<#list relationList as relation>
			<div ${vueGenerator.getSubAttrs(relation)} >
				<div class="block-title"> <span class="title">${relation.tableComment} </span>
					${getOne2ManyChild(relation)}
				</div>
				<table class="form-table">
					<#list relation.table.columnsWithOutHidden as column>
						<tr>
							<th>${column.comment}</th>
							<td>${vueGenerator.getColumn(column,relation)} </td>
						</tr>
					</#list>
				</table>
				${getOne2OneChild(relation)}
			</div>
		</#list>
	</#assign>
	<#return rtn>
</#function>

<#function getOne2ManyChild relation> 
	<#assign relationList = relation.getChildren('oneToMany')>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
		<div class="pull-left">
		<#list relationList as relation>
		<ab-sub-detail v-bind:sub-temp-data.sync="subTempData" href="javascript:void(0)" class="btn btn-link btn-sm fa fa-detail" pkey="${relation.parent.tableKey}" v-model="${vueGenerator.getScopePath(relation.parent)}[index]" dialog-id="${relation.getBusObj().getKey()}-${relation.tableKey}" v-ab-permission:show="scope.tablePermission.${relation.busObj.key}.${relation.tableKey}" >${relation.tableComment}详情</ab-sub-detail>
		</#list>
		</div>
		</#if>
	</#assign>
	<#return rtn>
</#function>