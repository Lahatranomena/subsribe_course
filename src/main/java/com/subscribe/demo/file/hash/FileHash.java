package com.subscribe.demo.file.hash;

import com.subscribe.demo.PojaGenerated;

@PojaGenerated
public record FileHash(FileHashAlgorithm algorithm, String value) {}
