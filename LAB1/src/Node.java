
public class Node {
	Node mNext; 
	Node mPrevious;
	int mDistance;
	int mUserState;
	int mX;
	int mY;

    public Node() {
    	this.mNext = null;
    	this.mPrevious = null;
    }
    
    public void setPrevious(Node previous) {
        this.mPrevious = previous;
    }
    
    public Node getPrevious() {
        return this.mPrevious;
    }
    
    public void setNext(Node next) {
    	this.mNext = next;
    }
    
    public Node getNext() {
        return this.mNext;
    }
    
    public void setDistance(int distance) {
    	this.mDistance = distance;
    }
    
    public int getDistance() {
    	return this.mDistance;
    }

    public void setUserState(int userstate) {
    	this.mUserState = userstate;
    }
    
    public int getUserState() {
    	return this.mUserState;
    }
    
    public void setX(int x) {
    	this.mX = x;
    }
    
    public int getX() {
    	return this.mX;
    }
    
    public void setY(int y) {
    	this.mY = y;
    }
    
    public int getY() {
    	return this.mY;
    }
}
