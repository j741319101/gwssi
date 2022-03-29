package com.dstz.bpm.wf.rest.vo;

import java.io.Serializable;
import java.util.List;

public class DocAndEnclosureDto implements Serializable {
   private String contentId;
   private String businessId;
   private String documentName;
   private String contentCode;
   private String documentBody;
   private String source;
   private String describe;
   List<DocumentEnclosureDto> documentEnclosureDtos;

   public String getContentId() {
      return this.contentId;
   }

   public void setContentId(String contentId) {
      this.contentId = contentId;
   }

   public String getBusinessId() {
      return this.businessId;
   }

   public void setBusinessId(String businessId) {
      this.businessId = businessId;
   }

   public String getDocumentName() {
      return this.documentName;
   }

   public void setDocumentName(String documentName) {
      this.documentName = documentName;
   }

   public String getContentCode() {
      return this.contentCode;
   }

   public void setContentCode(String contentCode) {
      this.contentCode = contentCode;
   }

   public String getDocumentBody() {
      return this.documentBody;
   }

   public void setDocumentBody(String documentBody) {
      this.documentBody = documentBody;
   }

   public String getSource() {
      return this.source;
   }

   public void setSource(String source) {
      this.source = source;
   }

   public String getDescribe() {
      return this.describe;
   }

   public void setDescribe(String describe) {
      this.describe = describe;
   }

   public List<DocumentEnclosureDto> getDocumentEnclosureDtos() {
      return this.documentEnclosureDtos;
   }

   public void setDocumentEnclosureDtos(List<DocumentEnclosureDto> documentEnclosureDtos) {
      this.documentEnclosureDtos = documentEnclosureDtos;
   }
}
