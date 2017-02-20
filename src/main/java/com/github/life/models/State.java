package com.github.life.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class State {
	private Map<String, List<Integer>> rules;
	private List<Block> blocks;
}
