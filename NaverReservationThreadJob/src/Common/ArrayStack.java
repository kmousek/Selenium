package Common;

public class ArrayStack {
	int top;
	int stackSize;
	int stackArr[];
	
	public ArrayStack(int stackSize) {
		top = -1;
		this.stackSize = stackSize;
		stackArr = new int[stackSize];
	}
	
	public int getTop() {
		return this.top;
	}
	
	public boolean isEmpty() {
		return (top == -1);
	}
	
	public boolean isFull() {
		return (top == this.stackSize-1);
	}
	
	public void push(int item) {
		if(isFull()) {
			System.out.println("stack is Full");
		}else {
			stackArr[++top] = item;
			System.out.println("Inserted Item : " + item);
		}
	}
	
	//추출 후 삭제
	public int pop() {
		if(isEmpty()) {
			System.out.println("pop fail! stack is empty!");
			return 0;
		}else {
			System.out.println("pop : " + stackArr[top]);
			return stackArr[top--];
		}
	}
	
	//추출만
	public int peek() {
        if(isEmpty()) {
            System.out.println("Peeking fail! Stack is empty!");
            return 0;
        } else { 
            System.out.println("Peeked Item : " + stackArr[top]);
            return stackArr[top];
        }
    }
	
	
	//전체 추출
	public int[] peekAll() {
		for(int i=0;i<=top;i++) {
			System.out.print(stackArr[i]+" ");
		}
		System.out.println();
        return this.stackArr;
	}
	
    public void clear() {
        if(isEmpty()) {
            System.out.println("Stack is already empty!");
        } else {
            top = -1;    // 스택 포인터 초기화
            stackArr = new int[this.stackSize];    // 새로운 스택 배열 생성
            System.out.println("Stack is clear!");
        }
    }
}
