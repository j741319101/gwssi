<div ${vueGenerator.getSubAttrs(relation)} v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}" >
	<div ${vueGenerator.getDivVIf(relation)}>
	<div class="ibox-title"><span class="title">${group.comment}</span>
		<ab-sub-add href="javascript:void(0)" class="btn btn-primary btn-sm fa fa-plus" v-model="${vueGenerator.getScopePath(relation)}" v-bind:init-data="initData.${relation.busObj.key}.${relation.tableKey}" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}">添加 </ab-sub-add>
	</div>
	<ab-sub-scope v-slot="scope" :permissiona="permission" :table-permissiona="tablePermission" v-for="(${relation.tableKey},index) in ${vueGenerator.getScopePath(relation)}"> 
		<div class="col-xs-12 panel-heading">
			${getOne2ManyChild(relation)}
			<ab-sub-del  class="btn btn-danger btn-sm fa fa-delete" v-model="${vueGenerator.getScopePath(relation)}" v-bind:index="index" v-ab-permission:edit="scope.tablePermission.${relation.busObj.key}.${relation.tableKey}">移除</ab-sub-del>
		</div>
		<div class="panel-body">
		<#list group.columnList as groupColumn>
			<div class="col-xs-${12/groupColumn.row} form-item" v-ab-permission:show="scope.permission.${relation.busObj.key}.${groupColumn.tableKey}.${groupColumn.key}">
				<label class="form-item-label">${groupColumn.comment}</label>
				<div class="form-item-content">${vueGenerator.getColumn(group,groupColumn)}</div>
			</div>
		</#list>
		</div>
	</ab-sub-scope>
	</div>
</div>

<#function getOne2ManyChild relation>
	<#assign relationList = relation.getChildren('oneToMany')>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
		 <div class="pull-left">
		<#list relationList as relation>
            <ab-sub-detail v-bind:sub-temp-data.sync="subTempData" pkey="${relation.parent.tableKey}" class="btn btn-primary btn-sm fa fa-detail"  v-model="${vueGenerator.getScopePath(relation.parent)}<#if relation.parent.type =='oneToMany'>[index]</#if>"  dialog-id="${relation.getBusObj().getKey()}-${relation.tableKey}" v-ab-permission:show="scope.tablePermission.${relation.busObj.key}.${relation.tableKey}" >${relation.tableComment}详情</ab-sub-detail>
		</#list>
		</div>
		</#if>
	</#assign>
	<#return rtn>
</#function>
