package com.app.common.dto;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class SliceResponse<T> {
	// 실제 데이터 리스트를 담는 필드
	protected final List<T> content;
	// 현재 페이지 번호 (1부터 시작하도록 +1)
	protected final int currentPage;
	// 한 페이지에 보여줄 데이터 수
	protected final int pageSize;
	// 첫 번째 페이지인지 여부를 나타내는 필드
	protected final boolean first;
	// 마지막 페이지인지 여부를 나타내는 필드
	protected final boolean last;

	// Slice 객체를 받아 SliceResponse 객체를 생성하는 생성자
	public SliceResponse(Slice<T> sliceContent) {
		// 데이터 리스트를 설정
		this.content = sliceContent.getContent();
		// 현재 페이지 번호를 설정 (0부터 시작하므로 +1)
		this.currentPage = sliceContent.getNumber() + 1;
		// 페이지 크기를 설정
		this.pageSize = sliceContent.getSize();
		// 첫 번째 페이지인지 여부를 설정
		this.first = sliceContent.isFirst();
		// 마지막 페이지인지 여부를 설정
		this.last = sliceContent.isLast();
	}
}
