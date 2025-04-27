package com.app.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {

    private int page = 1;  // 기본 페이지 번호
    private int size = 12; // 기본 페이지 크기

    public int getPage() {
        return page - 1;
    }

    public int getSize() {
        return size;
    }

    public PageRequest of() {
        return new PageRequest(this.page, this.size);
    }

}
