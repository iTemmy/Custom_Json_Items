package com.temmy.json_items_test_1.util;


public interface CustomItem {
  
  String name;
  Material material;
  Component displayName;
  
  super(name);
  
  void setname(Stirng name){
    this.name = name;
  }
  
  void setMaterial(Material material){
    this.material = material;
  }
  
  void setDisplayName(Component displayName){
    this.displayName = displayName;
  }
  
  String name(){
    return name;
  }
  
  Material getMaterial(){
    return material;
  }
  
  Component getDisplayName(){
    return displayName;
  }
}
