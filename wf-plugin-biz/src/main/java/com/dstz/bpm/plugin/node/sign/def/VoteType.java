package com.dstz.bpm.plugin.node.sign.def;

public enum VoteType {
   AMOUNT("amount", "投票数"),
   PERCENT("percent", "百分比");

   private String key = "";
   private String value = "";

   private VoteType(String key, String value) {
      this.key = key;
      this.value = value;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String toString() {
      return this.key;
   }

   public static VoteType fromKey(String key) {
      VoteType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         VoteType c = var1[var3];
         if (c.getKey().equalsIgnoreCase(key)) {
            return c;
         }
      }

      throw new IllegalArgumentException(key);
   }
}
