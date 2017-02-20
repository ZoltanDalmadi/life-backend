package com.github.life.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Block {
	private Map<String, Integer> start;
	private List<List<Integer>> cells;
}
