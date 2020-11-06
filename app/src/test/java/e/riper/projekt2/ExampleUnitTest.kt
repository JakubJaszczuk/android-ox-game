package e.riper.projekt2

import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    // Array2d

    @Test(expected = IndexOutOfBoundsException::class)
    fun array_set() {
        val a = Array2d<Int>(2, 2)
        a.set(10, 10, 2)
    }

    @Test
    fun array_safe_set() {
        val a = Array2d<Int>(2, 2)
        a.setSafe(10, 10, 2)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun array_get() {
        val a = Array2d<Int>(2, 2)
        a.get(10, 10)
    }

    @Test
    fun array_safe_get() {
        val a = Array2d<Int>(2, 2)
        a.getSafe(10, 10)
    }

    @Test
    fun array_safe_get_default1() {
        val a = Array2d<Int>(2, 2)
		val b = a.getBuffer()
		// Fill Buffer
		for(i in 0..3) {
			b.add(0)
		}
        val res = a.getSafeOrDefault(1, 1, -3)
        assertEquals(res, 0)
    }

    @Test
    fun array_safe_get_default2() {
        val a = Array2d<Int>(2, 2)
        val res = a.getSafeOrDefault(10, 10, -3)
        assertEquals(res, -3)

    }

    @Test
    fun array_convertIndexTo2d_1() {
        val a = Array2d<Int>(2, 2)
        val res = a.convertIndexTo2d(0)
        assertEquals(res, Pair(0, 0))
    }

    @Test
    fun array_convertIndexTo2d_2() {
        val a = Array2d<Int>(2, 2)
        val res = a.convertIndexTo2d(2)
        assertEquals(res, Pair(0, 1))
    }

    @Test
    fun array_convertIndexTo2d_3() {
        val a = Array2d<Int>(2, 2)
        val res = a.convertIndexTo2d(9)
        assertEquals(res, Pair(1, 4))
    }

    @Test
    fun array_convertToIndex_1() {
        val a = Array2d<Int>(2, 2)
        val res = a.convertToIndex(0, 0)
        assertEquals(res, 0)
    }

	@Test
	fun array_convertToIndex_2() {
		val a = Array2d<Int>(2, 2)
		val res = a.convertToIndex(0, 3)
		assertEquals(res, 6)
	}

	@Test
	fun array_convertToIndex_3() {
		val a = Array2d<Int>(2, 2)
		val res = a.convertToIndex(1, 3)
		assertEquals(res, 7)
	}


    // Board

	@Test
	fun board_one_placed() {
		val board: Board = Board()
		assertFalse(board.checkWin(3, 3, Cell.BLACK))
	}

	@Test
	fun board_check_row_1() {
		val board: Board = Board()
		val data = board.board
		data.set(2, 2, Cell.WHITE)
		data.set(3, 2, Cell.WHITE)
		data.set(4, 2, Cell.WHITE)
		data.set(5, 2, Cell.WHITE)
		assertFalse(board.checkWin(2, 2, Cell.WHITE))
	}

	@Test
	fun board_check_row_2() {
		val board: Board = Board()
		val data = board.board
		data.set(2, 2, Cell.WHITE)
		data.set(3, 2, Cell.WHITE)
		data.set(4, 2, Cell.WHITE)
		data.set(5, 2, Cell.WHITE)
		data.set(6, 2, Cell.WHITE)
		assertTrue(board.checkWin(2, 2, Cell.WHITE))
	}

	@Test
	fun board_check_row_3() {
		val board: Board = Board()
		val data = board.board
		data.set(2, 2, Cell.WHITE)
		data.set(3, 2, Cell.WHITE)
		data.set(4, 2, Cell.WHITE)
		data.set(5, 2, Cell.WHITE)
		data.set(6, 2, Cell.WHITE)
		assertTrue(board.checkWin(4, 2, Cell.WHITE))
	}

	@Test
	fun board_check_row_4() {
		val board: Board = Board()
		val data = board.board
		data.set(2, 2, Cell.WHITE)
		data.set(3, 2, Cell.WHITE)
		data.set(4, 2, Cell.BLACK)
		data.set(5, 2, Cell.WHITE)
		data.set(6, 2, Cell.WHITE)
		assertTrue(board.checkWin(4, 2, Cell.WHITE))
	}

	@Test
	fun board_check_row_5() {
		val board: Board = Board()
		val data = board.board
		data.set(2, 2, Cell.WHITE)
		data.set(3, 2, Cell.WHITE)
		data.set(4, 2, Cell.BLACK)
		data.set(5, 2, Cell.WHITE)
		data.set(6, 2, Cell.WHITE)
		data.set(7, 2, Cell.WHITE)
		data.set(8, 2, Cell.WHITE)
		data.set(9, 2, Cell.WHITE)
		assertTrue(board.checkWin(5, 2, Cell.WHITE))
	}
}
