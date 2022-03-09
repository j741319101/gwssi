<div ${vueGenerator.getSubAttrs(relation)} v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}" >
	<div class="ibox-title"><span class="title">${relation.tableComment}</span>
        <ab-sub-add class="btn btn-primary btn-sm fa fa-plus" v-model="${vueGenerator.getScopePath_old(relation)}" v-bind:init-data="initData.${relation.busObj.key}.${relation.tableKey}" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}">添加 </ab-sub-add>
	</div>
		<table class="form-table">
			<thead>
				<tr>
					<#list group.columnList as groupColumn>
					<th v-ab-permission:show="permission.${relation.busObj.key}.${groupColumn.tableKey}.${groupColumn.key}">${groupColumn.comment}</th>
					</#list>	
					<th>操作</th>
				</tr>
			</thead>
			<tr v-for="(${relation.tableKey},index) in ${vueGenerator.getScopePath_old(relation)}">
			<#list group.columnList as groupColumn>
				<td v-ab-permission:show="permission.${relation.busObj.key}.${groupColumn.tableKey}.${groupColumn.key}">${vueGenerator.getLineColumn(group,groupColumn)}</td>
			</#list>
				<td>${getOne2ManyChild(relation)}
                    	<ab-sub-del  class="btn btn-danger btn-sm fa fa-delete" v-model="${vueGenerator.getScopePath_old(relation)}" v-bind:index="index" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}">移除</ab-sub-del>
				</td>
			</tr>
		</table>
</div>

<#function getOne2ManyChild relation> 
	<#assign relationList = relation.getChildren('oneToMany')>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
		<#list relationList as relation>
            <ab-sub-detail class="btn btn-link btn-sm fa fa-detail"  v-model="${vueGenerator.getScopePath_old(relation.parent)}<#if relation.parent.type =='oneToMany'>[index]</#if>" dialog-id="${relation.getBusObj().getKey()}-${relation.tableKey}" v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}" >${relation.tableComment}详情</ab-sub-detail>
		</#list>
		</#if>
	</#assign>
	<#return rtn>
</#function>