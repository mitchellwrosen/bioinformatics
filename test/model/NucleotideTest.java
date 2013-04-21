package model;

import static org.junit.Assert.assertEquals;
import model.Nucleotide;

import org.junit.Test;

/**
 * @author Erik Sandberg
 *
 */
public class NucleotideTest {
	/**
	 * Test method for {@link model.Nucleotide#fromChar(char)}.
	 */
	@Test
	public void testFromChar() {
		assertEquals(Nucleotide.ADENINE, Nucleotide.fromChar('a'));
		assertEquals(Nucleotide.ADENINE, Nucleotide.fromChar('A'));
		assertEquals(Nucleotide.THYMINE	, Nucleotide.fromChar('t'));
		assertEquals(Nucleotide.THYMINE, Nucleotide.fromChar('T'));
		assertEquals(Nucleotide.GUANINE, Nucleotide.fromChar('g'));
		assertEquals(Nucleotide.GUANINE, Nucleotide.fromChar('G'));
		assertEquals(Nucleotide.CYTOSINE, Nucleotide.fromChar('c'));
		assertEquals(Nucleotide.CYTOSINE, Nucleotide.fromChar('C'));
		assertEquals(Nucleotide.UNKNOWN, Nucleotide.fromChar('n'));
		assertEquals(Nucleotide.UNKNOWN, Nucleotide.fromChar('N'));
	}

	/**
	 * Test method for {@link model.Nucleotide#complement()}.
	 */
	@Test
	public void testComplement() {
		assertEquals(Nucleotide.ADENINE, Nucleotide.THYMINE.complement());
		assertEquals(Nucleotide.THYMINE, Nucleotide.ADENINE.complement());
		assertEquals(Nucleotide.CYTOSINE, Nucleotide.GUANINE.complement());
		assertEquals(Nucleotide.GUANINE, Nucleotide.CYTOSINE.complement());
	}

}
