package com.github.life.services;

import com.github.life.errors.InvalidLifFileException;
import com.github.life.models.Block;
import com.github.life.models.State;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class ParserServiceTest {

	private static int testUniverseCols = 80;
	private static int testUniverseRows = 60;

	// service under test
	private ParserService parserService;

	@Before
	public void setUp() throws Exception {
		parserService = new ParserService();
	}

	/**
	 * Tests if a file with standard Conway rules parses successfully.
	 *
	 * @throws Exception if an error occurs.
	 */
	@Test
	public void parseNormalRulesAllOk() throws Exception {
		File file = ResourceUtils.getFile("classpath:acorn.lif");
		FileInputStream is = new FileInputStream(file);

		State testData = acornTestData();

		assertState(testData, is, testUniverseCols, testUniverseRows);
	}

	/**
	 * Tests if a file with custom rules parses successfully.
	 *
	 * @throws Exception if an error occurs.
	 */
	@Test
	public void parseCustomRulesAllOk() throws Exception {
		File file = ResourceUtils.getFile("classpath:acorn_custom_rule.lif");
		FileInputStream is = new FileInputStream(file);

		State testData = acornTestData();

		// Set up a custom rule
		Map<String, List<Integer>> customRule = new LinkedHashMap<>();
		customRule.put("toSurvive", Arrays.asList(1, 2, 5));
		customRule.put("toComeAlive", Arrays.asList(3, 6));
		testData.setRules(customRule);

		assertState(testData, is, testUniverseCols, testUniverseRows);
	}

	/**
	 * Tests if the standard Conway rules are applied
	 * if a file does not contain a rule definition.
	 *
	 * @throws Exception if an error occurs.
	 */
	@Test
	public void parseAppliesDefaultRulesIfNoneSpecified() throws Exception {
		File file = ResourceUtils.getFile("classpath:acorn_no_rule.lif");
		FileInputStream is = new FileInputStream(file);

		State testData = acornTestData();

		assertState(testData, is, testUniverseCols, testUniverseRows);
	}

	/**
	 * Tests if an {@link InvalidLifFileException} is thrown when
	 * the input file contains an invalid character in the block data.
	 *
	 * @throws InvalidLifFileException if an invalid character has been found.
	 */
	@Test(expected = InvalidLifFileException.class)
	public void parseThrowsIfInvalidCharFound() throws Exception {
		File file = ResourceUtils.getFile("classpath:acorn_invalid_char.lif");
		FileInputStream is = new FileInputStream(file);

		State testData = acornTestData();

		assertState(testData, is, testUniverseCols, testUniverseRows);
	}

	/**
	 * Asserts that the parsed result matches the provided expected {@link State}.
	 *
	 * @param testData Expected parsing result
	 * @param is       {@link InputStream} of the file
	 * @param cols     Universe columns
	 * @param rows     Universe rows
	 * @throws Exception if an error occurs.
	 */
	private void assertState(State testData, InputStream is, int cols, int rows) throws Exception {
		State result = parserService.parse(is, cols, rows);
		is.close();

		assertEquals(testData.getRules().get("toSurvive"), result.getRules().get("toSurvive"));
		assertEquals(testData.getRules().get("toComeAlive"), result.getRules().get("toComeAlive"));

		List<Block> expectedBlocks = testData.getBlocks();
		List<Block> resultBlocks = result.getBlocks();

		assertEquals(expectedBlocks.size(), resultBlocks.size());

		for (int i = 0; i < expectedBlocks.size(); i++) {
			Block currentExpected = expectedBlocks.get(i);
			Block currentResult = resultBlocks.get(i);
			assertEquals(currentExpected.getStart(), currentResult.getStart());
			assertEquals(currentExpected.getCells(), currentResult.getCells());
		}
	}

	/**
	 * Returns the expected result of parsing the "acorn.lif" file.
	 *
	 * @return The {@link State} object of the expected result.
	 */
	private State acornTestData() {
		Map<String, List<Integer>> rules = new LinkedHashMap<>();
		rules.put("toSurvive", Arrays.asList(2, 3));
		rules.put("toComeAlive", Collections.singletonList(3));

		Map<String, Integer> start = new LinkedHashMap<>();

		// assume a 80x60 universe
		start.put("x", 37);
		start.put("y", 31);

		List<List<Integer>> cells = new ArrayList<>();
		cells.add(Arrays.asList(0, 1));
		cells.add(Arrays.asList(0, 0, 0, 1));
		cells.add(Arrays.asList(1, 1, 0, 0, 1, 1, 1));

		Block block = new Block();
		block.setStart(start);
		block.setCells(cells);

		List<Block> blocks = new ArrayList<>();
		blocks.add(block);

		State testData = new State();
		testData.setRules(rules);
		testData.setBlocks(blocks);

		return testData;
	}
}
