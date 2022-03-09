/*     */ package com.dstz.bpm.act.img;
/*     */ 
/*     */ import com.dstz.base.core.util.ThreadMapUtil;
/*     */ import java.awt.Color;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.font.LineBreakMeasurer;
/*     */ import java.awt.font.TextAttribute;
/*     */ import java.awt.font.TextLayout;
/*     */ import java.awt.geom.Line2D;
/*     */ import java.awt.geom.RoundRectangle2D;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.AttributedString;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.activiti.bpmn.model.AssociationDirection;
/*     */ import org.activiti.bpmn.model.FlowNode;
/*     */ import org.activiti.bpmn.model.GraphicInfo;
/*     */ import org.activiti.image.impl.DefaultProcessDiagramCanvas;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BpmProcessDiagramCanvas
/*     */   extends DefaultProcessDiagramCanvas
/*     */ {
/*     */   public BpmProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType, String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader) {
/*  36 */     super(width, height, minX, minY, imageType, activityFontName, labelFontName, annotationFontName, customClassLoader);
/*  37 */     LABEL_COLOR = Color.BLACK;
/*  38 */     LABEL_FONT = null;
/*     */   }
/*     */   
/*     */   public void drawHighLight(int x, int y, int width, int height, Paint paint) {
/*  42 */     Paint originalPaint = this.g.getPaint();
/*  43 */     Stroke originalStroke = this.g.getStroke();
/*     */     
/*  45 */     this.g.setPaint(paint);
/*  46 */     this.g.setStroke(THICK_TASK_BORDER_STROKE);
/*     */     
/*  48 */     RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 20.0D, 20.0D);
/*  49 */     this.g.draw(rect);
/*     */     
/*  51 */     this.g.setPaint(originalPaint);
/*  52 */     this.g.setStroke(originalStroke);
/*     */   }
/*     */   
/*     */   public void drawSequenceflow(int[] xPoints, int[] yPoints, boolean drawConditionalIndicator, boolean isDefault, boolean highLighted, Paint paint, double scaleFactor) {
/*  56 */     String connectionType = "sequenceFlow";
/*  57 */     AssociationDirection associationDirection = AssociationDirection.ONE;
/*  58 */     boolean conditional = drawConditionalIndicator;
/*     */     
/*  60 */     Paint originalPaint = this.g.getPaint();
/*  61 */     Stroke originalStroke = this.g.getStroke();
/*     */     
/*  63 */     this.g.setPaint(CONNECTION_COLOR);
/*  64 */     if (connectionType.equals("association")) {
/*  65 */       this.g.setStroke(ASSOCIATION_STROKE);
/*  66 */     } else if (highLighted) {
/*  67 */       this.g.setPaint(paint);
/*  68 */       this.g.setStroke(HIGHLIGHT_FLOW_STROKE);
/*     */     } 
/*     */     
/*  71 */     for (int i = 1; i < xPoints.length; i++) {
/*  72 */       Integer sourceX = Integer.valueOf(xPoints[i - 1]);
/*  73 */       Integer sourceY = Integer.valueOf(yPoints[i - 1]);
/*  74 */       Integer targetX = Integer.valueOf(xPoints[i]);
/*  75 */       Integer targetY = Integer.valueOf(yPoints[i]);
/*  76 */       Line2D.Double line = new Line2D.Double(sourceX.intValue(), sourceY.intValue(), targetX.intValue(), targetY.intValue());
/*  77 */       this.g.draw(line);
/*     */     } 
/*     */     
/*  80 */     if (isDefault) {
/*  81 */       Line2D.Double line = new Line2D.Double(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
/*  82 */       drawDefaultSequenceFlowIndicator(line, scaleFactor);
/*     */     } 
/*     */     
/*  85 */     if (conditional) {
/*  86 */       Line2D.Double line = new Line2D.Double(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
/*  87 */       drawConditionalSequenceFlowIndicator(line, scaleFactor);
/*     */     } 
/*     */     
/*  90 */     if (associationDirection.equals(AssociationDirection.ONE) || associationDirection.equals(AssociationDirection.BOTH)) {
/*  91 */       Line2D.Double line = new Line2D.Double(xPoints[xPoints.length - 2], yPoints[xPoints.length - 2], xPoints[xPoints.length - 1], yPoints[xPoints.length - 1]);
/*  92 */       drawArrowHead(line, scaleFactor);
/*     */     } 
/*  94 */     if (associationDirection.equals(AssociationDirection.BOTH)) {
/*  95 */       Line2D.Double line = new Line2D.Double(xPoints[1], yPoints[1], xPoints[0], yPoints[0]);
/*  96 */       drawArrowHead(line, scaleFactor);
/*     */     } 
/*  98 */     this.g.setPaint(originalPaint);
/*  99 */     this.g.setStroke(originalStroke);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawExclusiveGateway(GraphicInfo graphicInfo, double scaleFactor) {
/* 107 */     Paint paint = this.g.getPaint();
/* 108 */     Map<String, Paint> gateMap = (Map<String, Paint>)ThreadMapUtil.get("DefaultInstHistImgService_gateMap");
/* 109 */     FlowNode flowNode = (FlowNode)ThreadMapUtil.get("BpmProcessDiagramGenerator_flowNode");
/* 110 */     this.g.setPaint(gateMap.getOrDefault(flowNode.getId(), paint));
/* 111 */     super.drawExclusiveGateway(graphicInfo, scaleFactor);
/* 112 */     this.g.setPaint(paint);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void drawMultilineText(String text, int x, int y, int boxWidth, int boxHeight, boolean centered) {
/* 118 */     AttributedString attributedString = new AttributedString(text);
/* 119 */     attributedString.addAttribute(TextAttribute.FONT, this.g.getFont());
/* 120 */     attributedString.addAttribute(TextAttribute.FOREGROUND, Color.black);
/*     */     
/* 122 */     AttributedCharacterIterator characterIterator = attributedString.getIterator();
/*     */     
/* 124 */     int currentHeight = 0;
/*     */     
/* 126 */     List<TextLayout> layouts = new ArrayList<>();
/* 127 */     String lastLine = null;
/*     */     
/* 129 */     LineBreakMeasurer measurer = new LineBreakMeasurer(characterIterator, this.g.getFontRenderContext());
/*     */     
/* 131 */     TextLayout layout = null;
/* 132 */     while (measurer.getPosition() < characterIterator.getEndIndex() && currentHeight <= boxHeight) {
/*     */       
/* 134 */       int previousPosition = measurer.getPosition();
/*     */ 
/*     */       
/* 137 */       layout = measurer.nextLayout(boxWidth);
/*     */       
/* 139 */       lastLine = text.substring(previousPosition, measurer.getPosition());
/*     */       
/* 141 */       int height = Float.valueOf(layout.getDescent() + layout.getAscent() + layout.getLeading()).intValue();
/*     */       
/* 143 */       if (currentHeight + height > boxHeight) {
/*     */ 
/*     */ 
/*     */         
/* 147 */         if (!layouts.isEmpty()) {
/* 148 */           layouts.remove(layouts.size() - 1);
/*     */         }
/* 150 */         layouts.add(new TextLayout(lastLine, this.g.getFont(), this.g.getFontRenderContext()));
/*     */         break;
/*     */       } 
/* 153 */       layouts.add(layout);
/* 154 */       currentHeight += height;
/*     */     } 
/*     */ 
/*     */     
/* 158 */     int currentY = y + (centered ? ((boxHeight - currentHeight) / 2) : 0);
/* 159 */     int currentX = 0;
/*     */ 
/*     */     
/* 162 */     for (TextLayout textLayout : layouts) {
/*     */       
/* 164 */       currentY = (int)(currentY + textLayout.getAscent());
/* 165 */       currentX = x + (centered ? ((boxWidth - Double.valueOf(textLayout.getBounds().getWidth()).intValue()) / 2) : 0);
/* 166 */       textLayout.draw(this.g, currentX, currentY);
/* 167 */       currentY = (int)(currentY + textLayout.getDescent() + textLayout.getLeading());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/act/img/BpmProcessDiagramCanvas.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */