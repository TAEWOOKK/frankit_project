package com.app.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {
	// 실제 데이터 리스트를 담는 필드
	private final List<T> content;
	// 현재 페이지 번호 (1부터 시작하도록 +1)
	private final int currentPage;
	// 한 페이지에 보여줄 데이터 수
	private final int pageSize;
	// 전체 요소의 개수를 나타내는 필드
	private final long totalElements;
	// 전체 페이지 수를 나타내는 필드
	private final int totalPages;
	// 다음 페이지 여부 나타내는 필드
	private final Boolean hasNext;
	// 이전 페이지 여부 나타내는 필드
	private final Boolean hasPrevious;

	// Page 객체를 받아서 PageResponse 객체를 생성하는 생성자
	public PageResponse(Page<T> pageContent) {
		// 부모 클래스인 SliceResponse의 생성자를 호출하여 초기화
		this.content = pageContent.getContent();
		// 현재 페이지 번호를 설정 (0부터 시작하므로 +1)
		this.currentPage = pageContent.getNumber() + 1;
		// 페이지 크기를 설정
		this.pageSize = pageContent.getSize();
		// 전체 요소의 개수를 설정
		this.totalElements = pageContent.getTotalElements();
		// 전체 페이지 수를 설정
		this.totalPages = pageContent.getTotalPages();
		// 다음 페이지 여부를 설정
		this.hasNext = pageContent.hasNext();
		// 이전 페이지 여부를 설정
		this.hasPrevious = pageContent.hasPrevious();
	}
}
