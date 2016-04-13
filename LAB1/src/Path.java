
public class Path {
	private int mMaxNodes;
	private int mSearchingstate;
	private int mSteps;
	private Node mStartNode;
	private Node mGoalNode;
	private Node mCurrentNode;
	private boolean mCancelSearch;
	
	public Path(int maxNodes) {
		this.mMaxNodes = maxNodes;
	}
	
	public void doSearchStep() {
		// Do work...
		if (this.mCancelSearch)
			return;
		
		this.mSteps++;	
	}
	
	public void cancelSearch() {
		// Do work...
		this.mCancelSearch = true;
	}
	
	public void setStartNode(Node startnode) {
		this.mStartNode = startnode;
	}
	
	public Node getStartNode() {
		return this.mStartNode;
	}
	
	public void setGoalNode(Node goalnode) {
		this.mGoalNode = goalnode;
	}
	
	public Node getGoalNode() {
		return this.mGoalNode;
	}
	
	public int getStepCount() {
		return this.mSteps;
	}
	
	public Node getPreviousNode(Node node) {
		return node.getPrevious();
	}
	
	public Node getNextNode(Node node) {
		return node.getNext();
	}
	
	public int getSearchingState() {
		return this.mSearchingstate;
	}
	
	public Node getCurrentNode() {
		return this.mCurrentNode;
	}
}
