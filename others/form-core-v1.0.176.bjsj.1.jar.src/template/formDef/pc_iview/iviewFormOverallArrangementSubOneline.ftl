<<#if iviewGenerator.isMultilayer(relation)>Modal width="80%"<#else>Card</#if> ${iviewGenerator.getSubAttrs(relation)} v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}" >
	<div <#if iviewGenerator.isMultilayer(relation)>slot="header"<#else>slot="title"</#if> ><span class="title">${relation.tableComment}</span>
        <ab-sub-add class="ivu-btn ivu-btn-primary" v-model="${iviewGenerator.getScopePath_old(relation)}" v-bind:init-data="initData.${relation.busObj.key}.${relation.tableKey}" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}">添加 </ab-sub-add>
	</div>
	<div>
		<table class="form-table">
			<thead>
				<tr>
					<#list group.columnList as groupColumn>
					<th v-ab-permission:show="permission.${relation.busObj.key}.${groupColumn.tableKey}.${groupColumn.key}">${groupColumn.comment}</th>
					</#list>	
					<th>操作</th>
				</tr>
			</thead>
			<tr v-for="(${relation.tableKey},index) in ${iviewGenerator.getScopePath_old(relation)}">
			<#list group.columnList as groupColumn>
				<td v-ab-permission:show="permission.${relation.busObj.key}.${groupColumn.tableKey}.${groupColumn.key}">${iviewGenerator.getLineColumn(group,groupColumn)}</td>
			</#list>
				<td> <button-group>${getOne2ManyChild(relation)}
                    	<ab-sub-del  class="ivu-btn ivu-btn-error" v-model="${iviewGenerator.getScopePath_old(relation)}" v-bind:index="index" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}">移除</ab-sub-del>
					</button-group>
				</td>
			</tr>
		</table>
	</div>
</<#if iviewGenerator.isMultilayer(relation)>Modal<#else>Card</#if>>

<#function getOne2ManyChild relation> 
	<#assign relationList = relation.getChildren('oneToMany')>
	<#assign rtn>
		 <#if relationList?? && (relationList?size > 0) >
		<#list relationList as relation>
            <ab-sub-detail class="ivu-btn ivu-btn-default"  v-model="${iviewGenerator.getScopePath_old(relation.parent)}<#if relation.parent.type =='oneToMany'>[index]</#if>" table-key="${relation.getBusObj().getKey()}_${relation.tableKey}" v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}" >${relation.tableComment}详情</ab-sub-detail>
		</#list>
		</#if>
	</#assign>
	<#return rtn>
</#function>