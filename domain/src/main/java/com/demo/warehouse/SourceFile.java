package com.demo.warehouse;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SourceFile {

	private String fileName;
	private FileType type;
}
