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

public class Rook extends Piece{


    private final static int[] CANDIDATE_MOVE_COORDINATES = {-8,-1,1,8};

    public Rook(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {


        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset:CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if(BoardUtils.isValidTileCoordinte(candidateDestinationCoordinate)){
                if(isFirstColumnExclusion(this.piecePosition,currentCandidateOffset) ||
                        isEightColumnExclusion(this.piecePosition,currentCandidateOffset)) {
                    continue;
                }



                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate) /* pass the legal move for the knight*/ );
                }else{
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if(this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new Move.AttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
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

        return BoardUtils.FIRST_COLUMN[currentPosition] &&(candidateOffset == -1);
    }



    private static boolean isEightColumnExclusion (final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset == 1);
    }
}
