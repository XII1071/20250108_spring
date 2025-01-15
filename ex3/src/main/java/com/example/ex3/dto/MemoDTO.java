package com.example.ex3.dto;

public class MemoDTO {
  private Long mno;
  private String memoText;

  public MemoDTO(Long mno, String memoText) {
    this.mno = mno;this.memoText = memoText;
  }
  public MemoDTO() {  }

  public Long getMno() {return mno;}
  public void setMno(Long mno) {this.mno = mno;}

  public String getMemoText() {return memoText;}
  public void setMemoText(String memoText) {
    this.memoText = memoText;
  }

  @Override
  public String toString() {
    return mno+":"+memoText;
  }
}
