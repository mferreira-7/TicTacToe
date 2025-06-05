import java.util.*;

// 1 = X (AI)
// 2 = O (USER)

//2) To add a method for calculating the heuristic value of any given game state (board), which could be added in the Board class or the AI_Player class;
//3) To modify the minimax method for returning values of non-endgame states rather than endgame states if the depth is too large (larger than a preset maximum depth, say, 6).

class AIplayer {
    List<Point> availablePoints;
    List<PointsAndScores> rootsChildrenScores;
    Board b = new Board();

    public AIplayer() {
    }

    public Point returnBestMove() { //method to choose the node by returning the node with the highest score
        int MAX = -100000;
        int best = -1;

        for (int i = 0; i < rootsChildrenScores.size(); ++i) {
            if (MAX < rootsChildrenScores.get(i).score) {
                MAX = rootsChildrenScores.get(i).score;
                best = i;
            }
        }
        return rootsChildrenScores.get(best).point;
    }

    public int returnMin(List<Integer> list) { //method to get the minimum value in a list - likely used for MinMax
        int min = Integer.MAX_VALUE;
        int index = -1;

        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) < min) {
                 min = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    public int returnMax(List<Integer> list) { //method to get the maximum value in a list - likely used for MinMax
        int max = Integer.MIN_VALUE;
        int index = -1;

        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) > max) {
                max = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    public void callMinimax(int depth, int turn, Board b){ //self-explanatory, calls the MinMax function
        rootsChildrenScores = new ArrayList<>(); //also instantiates the array
        minimax(depth, turn, b);
    }

    private int evaluateBoard(Board b) { //idea for this heuristic logic was inspired by a Youtube video on game playing AI but not copied at all
        int boardScore = 0;

        // Evaluate score for each of the 12 lines (5 rows, 5 columns, 2 diagonals)
        boardScore += evaluateLine(0, 0, 0, 1, 0, 2,0,3,0,4, b);  // row 0
        boardScore += evaluateLine(1, 0, 1, 1, 1, 2,1,3,1,4, b);  // row 1
        boardScore += evaluateLine(2, 0, 2, 1, 2, 2,2,3,2,4, b);  // row 2
        boardScore += evaluateLine(3, 0, 3, 1, 3, 2,3,3,3,4, b);  // row 3
        boardScore += evaluateLine(4, 0, 4, 1, 4, 2,4,3,4,4, b);  // row 4

        boardScore += evaluateLine(0, 0, 1, 0, 2, 0,3,0,4,0, b);  // col 0
        boardScore += evaluateLine(0, 1, 1, 1, 2, 1,3,1,4,1, b);  // col 1
        boardScore += evaluateLine(0, 2, 1, 2, 2, 2,3,2,4,2, b);  // col 2
        boardScore += evaluateLine(0, 3, 1, 3, 2, 3,3,3,4,3, b);  // col 3
        boardScore += evaluateLine(0, 4, 1, 4, 2, 4,3,4,4,4, b);  // col 4

        boardScore += evaluateLine(0, 0, 1, 1, 2, 2,3,3,4,4, b);  // leading diagonal
        boardScore += evaluateLine(0, 4, 1, 3, 2, 2,3,1,4,0, b);  // alternate diagonal

        return boardScore; //the total of the 12 lines' scores is the score for the board (game state)
    }

    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3, int row4, int col4, int row5, int col5, Board b) {
        //heuristic function = "the score of each line starts from 1 and is multiplied by 10 each time the wanted icon is found"
        //                     "if the opponent's icon is found in the same line, the line's score defaults to 0"
        //                     "if an empty cell is found the score goes back to 1"

        int score = 0;

        // First cell
        if (b.board[row1][col1] == 2) {
            score = 1;
        } else if (b.board[row1][col1] == 1) {
            score = -1;
        }

        // Second cell
        if (b.board[row2][col2] == 2) {
            if (score == 1) {   // cell1 is O
                score = 10;
            } else if (score == -1) {  // cell1 is X
                return 0;
            } else {  // cell1 is empty
                score = 1;
            }
        } else if (b.board[row2][col2] == 1) {
            if (score == -1) { // cell1 is X
                score = -10;
            } else if (score == 1) { // cell1 is O
                return 0;
            } else {  // cell1 is empty
                score = -1;
            }
        }

        // Third cell
        if (b.board[row3][col3] == 2) {
            if (score > 0) {  // cell1 and/or cell2 is O
                score *= 10;
            } else if (score < 0) {  // cell1 and/or cell2 is X
                return 0;
            } else {  // cell1 and cell2 are empty
                score = 1;
            }
        } else if (b.board[row3][col3] == 1) {
            if (score < 0) {  // cell1 and/or cell2 is X
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 is O
                return 0;
            } else {  // cell1 and cell2 are empty
                score = -1;
            }
        }

        //Fourth cell
        if (b.board[row4][col4] == 2) {
            if (score > 0) {  // cell1 and/or cell2 and/or cell3 is O
                score *= 10;
            } else if (score < 0) {  // cell1 and/or cell2 and/or cell3 is X
                return 0;
            } else {  // cell1,cell2 and cell3 are empty
                score = 1;
            }
        } else if (b.board[row4][col4] == 1) {
            if (score < 0) {  // cell1 and/or cell2 and/or cell3 is X
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 and/or cell3 is O
                return 0;
            } else {  // cell1,cell2 and cell3 are empty
                score = -1;
            }
        }

        //Fifth cell
        if (b.board[row5][col5] == 2) {
            if (score > 0) {  // cell1 and/or cell2 and/or cell3 and/or cell4 is O
                score *= 10;
            } else if (score < 0) {  // cell1 and/or cell2 and/or cell3 and/or cell4 is X
                return 0;
            } else {  // cell1,cell2,cell3 and cell4 are empty
                score = 1;
            }
        } else if (b.board[row5][col5] == 1) {
            if (score < 0) {  // cell1 and/or cell2 and/or cell3 and/or cell4 is X
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 and/or cell3 and/or cell4 is O
                return 0;
            } else {  // cell1,cell2,cell3 and cell4 are empty
                score = -1;
            }
        }

        return score;
    }


    public int minimax(int depth, int turn, Board b) {
        if (depth <= 4) { //limit on the depth
            if (b.hasXWon()) return 1;
            if (b.hasOWon()) return -1;
            List<Point> pointsAvailable = b.getAvailablePoints();
            if (pointsAvailable.isEmpty()) return 0;

            List<Integer> scores = new ArrayList<>();

            for (int i = 0; i < pointsAvailable.size(); ++i) {
                Point point = pointsAvailable.get(i);
                if (turn == 1) {
                    b.placeAMove(point, 1);
                    int currentScore = minimax(depth + 1, 2, b);
                    scores.add(currentScore);
                    if (depth == 0)
                        rootsChildrenScores.add(new PointsAndScores(currentScore, point));
                } else if (turn == 2) {
                    b.placeAMove(point, 2);
                    scores.add(minimax(depth + 1, 1, b));
                }
                b.placeAMove(point, 0);
            }
            if (turn == 1) return returnMax(scores);
            return returnMin(scores);
        } else {
            //return values of non-endgame states rather than endgame states
            return evaluateBoard(b);
        }
    }
}
