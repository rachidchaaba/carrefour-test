package com.carrefour.test;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class Yatzy {

	private int[] dice;

	public Yatzy(int d1, int d2, int d3, int d4, int d5) {
		dice = new int[] { d1, d2, d3, d4, d5 };
	}

	public static int chance(int d1, int d2, int d3, int d4, int d5) {
		return d1 + d2 + d3 + d4 + d5;
	}

	public static int yatzy(int... dice) {
		boolean countFiveExists = IntStream.of(dice).boxed().collect(groupingBy(i -> i, counting())).values().stream()
				.anyMatch(count -> count == 5);
		if (countFiveExists) {
			return 50;
		}
		return 0;
	}

	public static int ones(int d1, int d2, int d3, int d4, int d5) {
		return numbers(1, d1, d2, d3, d4, d5);
	}

	public static int twos(int d1, int d2, int d3, int d4, int d5) {
		return numbers(2, d1, d2, d3, d4, d5);
	}

	public static int threes(int d1, int d2, int d3, int d4, int d5) {
		return numbers(3, d1, d2, d3, d4, d5);
	}

	public int fours() {
		return numbers(4, dice);
	}

	public int fives() {
		return numbers(5, dice);
	}

	public int sixes() {
		return numbers(6, dice);
	}

	public static int score_pair(int d1, int d2, int d3, int d4, int d5) {
		int[] counts = getCounts(d1, d2, d3, d4, d5);

		OptionalInt OptionalIndex = IntStream.range(0, 6).filter(i -> counts[6 - i - 1] >= 2).findFirst();
		if (OptionalIndex.isPresent()) {
			return (6 - OptionalIndex.getAsInt()) * 2;
		}
		return 0;
	}

	public static int two_pair(int d1, int d2, int d3, int d4, int d5) {
		int[] counts = getCounts(d1, d2, d3, d4, d5);

		List<Integer> scores = IntStream.range(0, 6).boxed().filter(i -> counts[6 - i - 1] >= 2).map(i -> 6 - i)
				.collect(toList());

		if (scores.size() == 2) {
			return scores.stream().mapToInt(Integer::intValue).sum() * 2;
		}
		return 0;
	}

	public static int four_of_a_kind(int d1, int d2, int d3, int d4, int d5) {
		return number_of_a_kind(4, d1, d2, d3, d4, d5);
	}

	public static int three_of_a_kind(int d1, int d2, int d3, int d4, int d5) {
		return number_of_a_kind(3, d1, d2, d3, d4, d5);
	}

	public static int smallStraight(int d1, int d2, int d3, int d4, int d5) {
		int[] tallies = getCounts(d1, d2, d3, d4, d5);

		return straight(15, tallies, 0, 5);
	}

	public static int largeStraight(int d1, int d2, int d3, int d4, int d5) {
		int[] tallies = getCounts(d1, d2, d3, d4, d5);

		return straight(20, tallies, 1, 6);
	}

	public static int fullHouse(int d1, int d2, int d3, int d4, int d5) {
		int[] tallies = getCounts(d1, d2, d3, d4, d5);

		int tallie2Index = getTallieNumberIndex(tallies, 2);
		int tallie3Index = getTallieNumberIndex(tallies, 3);

		if (tallie2Index != -1 && tallie3Index != -1) {
			return (tallie2Index + 1) * 2 + (tallie3Index + 1) * 3;
		}
		return 0;
	}

	private static int numbers(int number, int... dice) {
		return IntStream.of(dice).boxed().filter(d -> d == number).collect(toList()).size() * number;
	}

	private static int[] getCounts(int d1, int d2, int d3, int d4, int d5) {
		int[] counts = new int[6];

		IntStream.of(d1, d2, d3, d4, d5).forEach(d -> {
			counts[d - 1]++;
		});
		return counts;
	}

	private static int number_of_a_kind(int number, int d1, int d2, int d3, int d4, int d5) {
		int[] tallies = getCounts(d1, d2, d3, d4, d5);

		int tallieGtNumberIndex = IntStream.range(0, 6).filter(i -> tallies[i] >= number).findFirst().orElse(-1);

		if (tallieGtNumberIndex != -1) {
			return (tallieGtNumberIndex + 1) * number;
		}
		return 0;
	}

	private static int straight(int numberToReturnIfStraight, int[] tallies, int tallieFirstIndex,
			int tallieLastIndex) {
		boolean allTalliesEquals1 = IntStream.range(tallieFirstIndex, tallieLastIndex).allMatch(i -> tallies[i] == 1);
		if (allTalliesEquals1) {
			return numberToReturnIfStraight;
		}
		return 0;
	}

	private static int getTallieNumberIndex(int[] tallies, int number) {
		int tallie2Index = IntStream.range(0, 6).boxed().filter(i -> tallies[i] == number).findFirst().orElse(-1);
		return tallie2Index;
	}
}
