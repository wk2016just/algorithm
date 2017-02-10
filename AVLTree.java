package avl;


public class AVLTree<T extends Comparable<T>> {
    private AVLTreeNode<T> mRoot;    // �����

    // AVL���Ľڵ�(�ڲ���)
    class AVLTreeNode<T extends Comparable<T>> {
        T key;                // �ؼ���(��ֵ)
        int height;         // �߶�
        AVLTreeNode<T> left;    // ����
        AVLTreeNode<T> right;    // �Һ���

        public AVLTreeNode(T key, AVLTreeNode<T> left, AVLTreeNode<T> right) {
            this.key = key;
            this.left = left;
            this.right = right;
            this.height = 0;
        }
    }

    // ���캯��
    public AVLTree() {
        mRoot = null;
    }

    /*
     * ��ȡ���ĸ߶�
     */
    private int height(AVLTreeNode<T> tree) {
        if (tree != null)
            return tree.height;

        return 0;
    }

    public int height() {
        return height(mRoot);
    }

    /*
     * �Ƚ�����ֵ�Ĵ�С
     */
    private int max(int a, int b) {
        return a>b ? a : b;
    }

    /*
     * ǰ�����"AVL��"
     */
    private void preOrder(AVLTreeNode<T> tree) {
        if(tree != null) {
            System.out.print(tree.key+" ");
            preOrder(tree.left);
            preOrder(tree.right);
        }
    }

    public void preOrder() {
        preOrder(mRoot);
    }

    /*
     * �������"AVL��"
     */
    private void inOrder(AVLTreeNode<T> tree) {
        if(tree != null)
        {
            inOrder(tree.left);
            System.out.print(tree.key+" ");
            inOrder(tree.right);
        }
    }

    public void inOrder() {
        inOrder(mRoot);
    }

    /*
     * �������"AVL��"
     */
    private void postOrder(AVLTreeNode<T> tree) {
        if(tree != null) {
            postOrder(tree.left);
            postOrder(tree.right);
            System.out.print(tree.key+" ");
        }
    }

    public void postOrder() {
        postOrder(mRoot);
    }

    /*
     * (�ݹ�ʵ��)����"AVL��x"�м�ֵΪkey�Ľڵ�
     */
    private AVLTreeNode<T> search(AVLTreeNode<T> x, T key) {
        if (x==null)
            return x;

        int cmp = key.compareTo(x.key);
        if (cmp < 0)
            return search(x.left, key);
        else if (cmp > 0)
            return search(x.right, key);
        else
            return x;
    }

    public AVLTreeNode<T> search(T key) {
        return search(mRoot, key);
    }

    /*
     * (�ǵݹ�ʵ��)����"AVL��x"�м�ֵΪkey�Ľڵ�
     */
    private AVLTreeNode<T> iterativeSearch(AVLTreeNode<T> x, T key) {
        while (x!=null) {
            int cmp = key.compareTo(x.key);

            if (cmp < 0)
                x = x.left;
            else if (cmp > 0)
                x = x.right;
            else
                return x;
        }

        return x;
    }

    public AVLTreeNode<T> iterativeSearch(T key) {
        return iterativeSearch(mRoot, key);
    }

    /* 
     * ������С��㣺����treeΪ������AVL������С��㡣
     */
    private AVLTreeNode<T> minimum(AVLTreeNode<T> tree) {
        if (tree == null)
            return null;

        while(tree.left != null)
            tree = tree.left;
        return tree;
    }

    public T minimum() {
        AVLTreeNode<T> p = minimum(mRoot);
        if (p != null)
            return p.key;

        return null;
    }
     
    /* 
     * ��������㣺����treeΪ������AVL��������㡣
     */
    private AVLTreeNode<T> maximum(AVLTreeNode<T> tree) {
        if (tree == null)
            return null;

        while(tree.right != null)
            tree = tree.right;
        return tree;
    }

    public T maximum() {
        AVLTreeNode<T> p = maximum(mRoot);
        if (p != null)
            return p.key;

        return null;
    }

    /*
     * LL�������Ӧ�����(����ת)��
     *
     * ����ֵ����ת��ĸ��ڵ�
     */
    private AVLTreeNode<T> leftLeftRotation(AVLTreeNode<T> k2) {
        AVLTreeNode<T> k1;

        k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;

        k2.height = max( height(k2.left), height(k2.right)) + 1;
        k1.height = max( height(k1.left), k2.height) + 1;

        return k1;
    }

    /*
     * RR�����Ҷ�Ӧ�����(�ҵ���ת)��
     *
     * ����ֵ����ת��ĸ��ڵ�
     */
    private AVLTreeNode<T> rightRightRotation(AVLTreeNode<T> k1) {
        AVLTreeNode<T> k2;

        k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;

        k1.height = max( height(k1.left), height(k1.right)) + 1;
        k2.height = max( height(k2.right), k1.height) + 1;

        return k2;
    }

    /*
     * LR�����Ҷ�Ӧ�����(��˫��ת)��
     *
     * ����ֵ����ת��ĸ��ڵ�
     */
    private AVLTreeNode<T> leftRightRotation(AVLTreeNode<T> k3) {
        k3.left = rightRightRotation(k3.left);

        return leftLeftRotation(k3);
    }

    /*
     * RL�������Ӧ�����(��˫��ת)��
     *
     * ����ֵ����ת��ĸ��ڵ�
     */
    private AVLTreeNode<T> rightLeftRotation(AVLTreeNode<T> k1) {
        k1.right = leftLeftRotation(k1.right);

        return rightRightRotation(k1);
    }

    /* 
     * �������뵽AVL���У������ظ��ڵ�
     *
     * ����˵����
     *     tree AVL���ĸ����
     *     key ����Ľ��ļ�ֵ
     * ����ֵ��
     *     ���ڵ�
     */
    private AVLTreeNode<T> insert(AVLTreeNode<T> tree, T key) {
        if (tree == null) {
            // �½��ڵ�
            tree = new AVLTreeNode<T>(key, null, null);
            if (tree==null) {
                System.out.println("ERROR: create avltree node failed!");
                return null;
            }
        } else {
            int cmp = key.compareTo(tree.key);

               if (cmp < 0) {    // Ӧ�ý�key���뵽"tree��������"�����
                tree.left = insert(tree.left, key);
                // ����ڵ����AVL��ʧȥƽ�⣬�������Ӧ�ĵ��ڡ�
                if (height(tree.left) - height(tree.right) == 2) {
                    if (key.compareTo(tree.left.key) < 0)
                        tree = leftLeftRotation(tree);
                    else
                        tree = leftRightRotation(tree);
                }
            } else if (cmp > 0) {    // Ӧ�ý�key���뵽"tree��������"�����
                tree.right = insert(tree.right, key);
                // ����ڵ����AVL��ʧȥƽ�⣬�������Ӧ�ĵ��ڡ�
                if (height(tree.right) - height(tree.left) == 2) {
                    if (key.compareTo(tree.right.key) > 0)
                        tree = rightRightRotation(tree);
                    else
                        tree = rightLeftRotation(tree);
                }
            } else {    // cmp==0
                System.out.println("���ʧ�ܣ������������ͬ�Ľڵ㣡");
            }
        }

        tree.height = max( height(tree.left), height(tree.right)) + 1;

        return tree;
    }

    public void insert(T key) {
        mRoot = insert(mRoot, key);
    }

    /* 
     * ɾ�����(z)�����ظ��ڵ�
     *
     * ����˵����
     *     tree AVL���ĸ����
     *     z ��ɾ���Ľ��
     * ����ֵ��
     *     ���ڵ�
     */
    private AVLTreeNode<T> remove(AVLTreeNode<T> tree, AVLTreeNode<T> z) {
        // ��Ϊ�� ���� û��Ҫɾ���Ľڵ㣬ֱ�ӷ���null��
        if (tree==null || z==null)
            return null;

        int cmp = z.key.compareTo(tree.key);
        if (cmp < 0) {        // ��ɾ���Ľڵ���"tree��������"��
            tree.left = remove(tree.left, z);
            // ɾ���ڵ����AVL��ʧȥƽ�⣬�������Ӧ�ĵ��ڡ�
            if (height(tree.right) - height(tree.left) == 2) {
                AVLTreeNode<T> r =  tree.right;
                if (height(r.left) > height(r.right))
                    tree = rightLeftRotation(tree);
                else
                    tree = rightRightRotation(tree);
            }
        } else if (cmp > 0) {    // ��ɾ���Ľڵ���"tree��������"��
            tree.right = remove(tree.right, z);
            // ɾ���ڵ����AVL��ʧȥƽ�⣬�������Ӧ�ĵ��ڡ�
            if (height(tree.left) - height(tree.right) == 2) {
                AVLTreeNode<T> l =  tree.left;
                if (height(l.right) > height(l.left))
                    tree = leftRightRotation(tree);
                else
                    tree = leftLeftRotation(tree);
            }
        } else {    // tree�Ƕ�ӦҪɾ���Ľڵ㡣
            // tree�����Һ��Ӷ��ǿ�
            if ((tree.left!=null) && (tree.right!=null)) {
                if (height(tree.left) > height(tree.right)) {
                    // ���tree�����������������ߣ�
                    // ��(01)�ҳ�tree���������е����ڵ�
                    //   (02)�������ڵ��ֵ��ֵ��tree��
                    //   (03)ɾ�������ڵ㡣
                    // ����������"tree�������������ڵ�"��"tree"������
                    // �������ַ�ʽ�ĺô��ǣ�ɾ��"tree�������������ڵ�"֮��AVL����Ȼ��ƽ��ġ�
                    AVLTreeNode<T> max = minimum(tree.left);
                    tree.key = max.key;
                    tree.left = remove(tree.left, max);
                } else {
                    // ���tree��������������������(��������ȣ�������������������1)
                    // ��(01)�ҳ�tree���������е���С�ڵ�
                    //   (02)������С�ڵ��ֵ��ֵ��tree��
                    //   (03)ɾ������С�ڵ㡣
                    // ����������"tree������������С�ڵ�"��"tree"������
                    // �������ַ�ʽ�ĺô��ǣ�ɾ��"tree������������С�ڵ�"֮��AVL����Ȼ��ƽ��ġ�
                    AVLTreeNode<T> min = maximum(tree.right);
                    tree.key = min.key;
                    tree.right = remove(tree.right, min);
                }
            } else {
                AVLTreeNode<T> tmp = tree;
                tree = (tree.left!=null) ? tree.left : tree.right;
                tmp = null;
            }
        }

        return tree;
    }

    public void remove(T key) {
        AVLTreeNode<T> z; 

        if ((z = search(mRoot, key)) != null)
            mRoot = remove(mRoot, z);
    }

    /* 
     * ����AVL��
     */
    private void destroy(AVLTreeNode<T> tree) {
        if (tree==null)
            return ;

        if (tree.left != null)
            destroy(tree.left);
        if (tree.right != null)
            destroy(tree.right);

        tree = null;
    }

    public void destroy() {
        destroy(mRoot);
    }

    /*
     * ��ӡ"���������"
     *
     * key        -- �ڵ�ļ�ֵ 
     * direction  --  0����ʾ�ýڵ��Ǹ��ڵ�;
     *               -1����ʾ�ýڵ������ĸ���������;
     *                1����ʾ�ýڵ������ĸ������Һ��ӡ�
     */
    private void print(AVLTreeNode<T> tree, T key, int direction) {
        if(tree != null) {
            if(direction==0)    // tree�Ǹ��ڵ�
                System.out.printf("%2d is root\n", tree.key, key);
            else                // tree�Ƿ�֧�ڵ�
                System.out.printf("%2d is %2d's %6s child\n", tree.key, key, direction==1?"right" : "left");

            print(tree.left, tree.key, -1);
            print(tree.right,tree.key,  1);
        }
    }

    public void print() {
        if (mRoot != null)
            print(mRoot, mRoot.key, 0);
    }
}