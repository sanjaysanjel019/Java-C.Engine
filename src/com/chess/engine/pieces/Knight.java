package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {


        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset:CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if(BoardUtils.isValidTileCoordinte(candidateDestinationCoordinate)){
              if(isFirstColumnExclusion(this.piecePosition,currentCandidateOffset) ||
                 isSecondColumneExclusion(this.piecePosition,currentCandidateOffset) ||
                 isSeventhColumnExclusion(this.piecePosition,currentCandidateOffset) ||
                 isEightColumnExclusion(this.piecePosition,currentCandidateOffset)) {
                  continue;
              }



                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate) /* pass the legal move for the knight*/ );
                }else{
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if(this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new AttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }



 /*
 * Calculate if Knight is sitting on the first Column of the Board
 * The offset values are where the rule breaks down and knight cannot move from the first column of the chess board
 * */
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
//        -17,-10,-6 and 15 are invalid moves for Knight if they are in the First Column.

        return BoardUtils.FIRST_COLUMN[currentPosition] &&(candidateOffset == -17 || candidateOffset == -10 || candidateOffset==6 || candidateOffset==15);
    }

    private  static boolean isSecondColumneExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset ==6);
    }

    private static boolean isSeventhColumnExclusion (final int currentPosition, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset==10);
    }

    private static boolean isEightColumnExclusion (final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset==-6 || candidateOffset==10  || candidateOffset==17);
    }
}
