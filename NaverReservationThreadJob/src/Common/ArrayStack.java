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
	
	//���� �� ����
	public int pop() {
		if(isEmpty()) {
			System.out.println("pop fail! stack is empty!");
			return 0;
		}else {
			System.out.println("pop : " + stackArr[top]);
			return stackArr[top--];
		}
	}
	
	//���⸸
	public int peek() {
        if(isEmpty()) {
            System.out.println("Peeking fail! Stack is empty!");
            return 0;
        } else { 
            System.out.println("Peeked Item : " + stackArr[top]);
            return stackArr[top];
        }
    }
	
	
	//��ü ����
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
            top = -1;    // ���� ������ �ʱ�ȭ
            stackArr = new int[this.stackSize];    // ���ο� ���� �迭 ����
            System.out.println("Stack is clear!");
        }
    }
}
