package tournament.backend;

public class BracketNode {
    private Match match;
    private BracketNode left; 
    private BracketNode right;

    public BracketNode(Match match) {
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }

    public BracketNode getLeft() {
        return left;
    }

    public void setLeft(BracketNode left) {
        this.left = left;
    }

    public BracketNode getRight() {
        return right;
    }

    public void setRight(BracketNode right) {
        this.right = right;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}
