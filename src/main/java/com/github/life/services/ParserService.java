package com.github.life.services;

import com.github.life.errors.InvalidLifFileException;
import com.github.life.models.Block;
import com.github.life.models.State;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
class ParserService {

	State parse(InputStream is, int universeCols, int universeRows) {
		List<Block> blocks = new ArrayList<>();
		Map<String, List<Integer>> rules = new LinkedHashMap<>();
		Block currentBlock = null;

		try (Scanner sc = new Scanner(is)) {
			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				// header or comment --------------------------------------------------------------
				if (line.startsWith("#Life") || line.startsWith("#D")) {
					continue;
				}

				// parse rules --------------------------------------------------------------------
				if (line.startsWith("#N")) {
					standardConwayRules(rules);
					continue;
				}

				if (line.startsWith("#R")) {
					String[] splits = line.substring(3).split("/");

					String toSurviveDigits = splits[0];
					String toComeAliveDigits = splits[1];

					List<Integer> toSurvive = toSurviveDigits.chars()
						.mapToObj(Character::getNumericValue).collect(Collectors.toList());
					List<Integer> toComeAlive = toComeAliveDigits.chars()
						.mapToObj(Character::getNumericValue).collect(Collectors.toList());

					rules.put("toSurvive", toSurvive);
					rules.put("toComeAlive", toComeAlive);
					continue;
				}

				// parse blocks -------------------------------------------------------------------
				if (line.startsWith("#P")) {
					String[] splits = line.substring(3).split(" ");

					// transform into our coordinate system (0, 0 in top left corner)
					int x = Integer.parseInt(splits[0]) + universeCols / 2;
					int y = -(Integer.parseInt(splits[1])) + universeRows / 2;

					// skip blocks that are outside of our universe
					if (!validCoords(x, y, universeCols, universeRows)) {
						continue;
					}

					Map<String, Integer> start = new LinkedHashMap<>();
					start.put("x", x);
					start.put("y", y);

					Block block = new Block();
					block.setStart(start);

					block.setCells(new ArrayList<>());

					currentBlock = block;

					blocks.add(block);

					continue;
				}

				// block data ---------------------------------------------------------------------
				if (Objects.nonNull(currentBlock)) {

					// skip adding new lines that does not fit into our universe
					int y = currentBlock.getStart().get("y");
					if (y + currentBlock.getCells().size() >= universeRows) {
						continue;
					}

					List<Integer> currentBlockLine = line.chars().mapToObj(value -> {
						switch (value) {
							case '.':
								return 0;
							case '*':
								return 1;
							default:
								throw new InvalidLifFileException("invalid character found in block data!");
						}
					}).collect(Collectors.toList());

					// "crop" blocks that does not fit into our universe
					int x = currentBlock.getStart().get("x");
					if (x + currentBlockLine.size() > universeCols) {
						currentBlockLine = currentBlockLine.stream()
							.limit(universeCols - x).collect(Collectors.toList());
					}

					currentBlock.getCells().add(currentBlockLine);
				}
			}
		}

		State state = new State();

		if (rules.isEmpty()) {
			standardConwayRules(rules);
		}

		state.setRules(rules);
		state.setBlocks(blocks);
		return state;
	}

	private void standardConwayRules(Map<String, List<Integer>> rules) {
		rules.put("toSurvive", Arrays.asList(2, 3));
		rules.put("toComeAlive", Collections.singletonList(3));
	}

	private boolean validCoords(int x, int y, int cols, int rows) {
		return x >= 0 && x < cols && y >= 0 && y < rows;
	}
}
