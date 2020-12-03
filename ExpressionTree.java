package hw3.student;
import java.text.ParseException;

//
//Resources used: https://www.youtube.com/watch?v=SH5F-rwWEog
/**
 * @author Sarthak Sharan CPSC 331
 * Used Recursive Descent for parsing
 */

public class ExpressionTree implements ExpressionTreeInterface {

     // index that the ExpressionTree parser hasn't looked at yet.
 private int index = 0;
 ExpressionTreeNode root;
 
 /**
 * Called during initialization and used to reset the parser.
 */
 
private void init() {
     index = 0;
     root = null;
 }
 /**
  * Constructor to build an empty parse tree.
  */
 public ExpressionTree() {
     init();
 }
 
 
 /**
 * @param line String containing the expression
 * @param idx Index of the current point where the parser is at in the expression
 * @return The character at the index
 * @throws ParseException If an error occurs at parsing
 */
private char viewIndex(String line, int idx) throws ParseException {
     if(idx < line.length())
         return line.charAt(idx);
     throw new ParseException("Expected more characters", idx);
 }
 /**
 * @param line String containing the expression
 * @param op1 Operator 1
 * @param op2 Operator 2
 * @return Returns true if the next character is op1 or op2
 */
private boolean isOneOfOperatorAhead(String line, char op1, char op2) {
     char x = line.charAt(index + 1);
     return x == op1 || x == op2;
 }
 /**
  * Check if the index is a whitespace and if so, goes to the next element
 * @param line String containing the expression
 * @throws ParseExceptionif an error occurs during parsing
 */
private void consumeWhitespace(String line) throws ParseException {
     if(viewIndex(line, index) != ' ')
         throw new ParseException("Expected white space but got" + viewIndex(line,index) , index);
     index ++;
 }
 
 /**
 * @param left node
 * @param line String containing the expression
 * @param op1,op2  ( "*" | "/" )  ( "+" | "-")
 * @param TorF If true, uses T to parse expression else uses F

 * @return A node that is a root node for the expression tree
 * @throws ParseException if an error occurs during parsing
 */
private ExpressionTreeNode parseZeroOrMoreTorF(ExpressionTreeNode left, String line, char op1, char op2, boolean TorF)
         throws ParseException {
     if(index + 1 < line.length() && isOneOfOperatorAhead(line, op1, op2)) {
         consumeWhitespace(line);
         char operator = viewIndex(line, index);
         index ++;
         consumeWhitespace(line);
         ExpressionTreeNode right = TorF ? T(line) : F(line);
         ExpressionTreeNode thisRootThatBecomesLeft = new ExpressionTreeNode(String.valueOf(operator), left, right);
         return parseZeroOrMoreTorF(thisRootThatBecomesLeft, line, op1, op2, TorF);
     }
     return left;
 }
 // Each of E, T, F consumes some number of elements from line
 // and then returns the number of elements it was able to consume.
 /**
 * @param line String containing the expression
 * @return SA node that is a root node for the expression tree
 * @throws ParseException if an error occurs during parsing
 */
private ExpressionTreeNode E(String line) throws ParseException {
     ExpressionTreeNode left = T(line);
     return parseZeroOrMoreTorF(left, line, '+', '-', true);
 }
 /**
 * @param line String containing the expression *
 * @return A node that is a root node for the expression tree
 * @throws ParseException if an error occurs during parsing
 */
private ExpressionTreeNode T(String line) throws ParseException {
     ExpressionTreeNode left = F(line);
     return parseZeroOrMoreTorF(left, line, '*', '/', false);
 }
 
 /**
  * Build a parse tree using grammar rule F. Expects line to be consumed from index.
  * @param line String containing the expression *
  * @throws ParseException If an error occurs during parsing. */
 private ExpressionTreeNode F(String line) throws ParseException {
     char front = viewIndex(line, index);
     if(front == '(') {
         // We are dealing with '( E )', first consume the opening bracket.
         index++;
         consumeWhitespace(line);
         // Now, expect an E.
         ExpressionTreeNode root = E(line);
         // Finally, get rid of the last closing bracket.
         consumeWhitespace(line);
         if(viewIndex(line, index) != ')')
             throw new ParseException("Expected ')' but got" + viewIndex(line,index), index);
         index ++;
         return root;
     } else {
         // It must be an integer then.
         int end = index;
         while(end != line.length() && line.charAt(end) != ' ') end ++;
         try {
             int num = Integer.parseInt(line.substring(index, end));
             index = end;
             return new ExpressionTreeNode(String.valueOf(num));
         } catch (NumberFormatException e) {
             throw new ParseException("Expected an integer but got" + viewIndex(line,index), index);
         }
     }
 }
 /**
  * Build a parse tree from an expression.
  * @param line String containing the expression *
  * @throws ParseException If an error occurs during parsing. */
 public void parse(String line) throws ParseException {
     init();
     root = E(line);
     if(index != line.length()) throw new ParseException("Expected to parse the expression but index is out of bounds", index);
 }
 /**
  * Evaluate the expression tree and return the integer result. An * empty tree returns 0.
  */
 public int evaluate() {
     if(root == null) return 0;
     return evaluate(root);
 }
 private int evaluate(ExpressionTreeNode root) {
     if(root.left == null) {
         // This implies both must be null.
         assert (root.right == null);
         return Integer.parseInt(root.el);
     } else {
         int left = evaluate(root.left);
         int right = evaluate(root.right);
         return applyOperator(root.el.charAt(0), left, right);
     }
 }
 /**
  * Applies the operator to x & y, returning x op y. Expects op to be '+'..
  */


 private int applyOperator(char op, int x, int y) {
     if(op == '+') return x + y;
     if(op == '-') return x - y;
     if(op == '/') return x / y;
     assert(op == '*');
     return x * y;
 }
 /**
  * Returns a postfix representation of the expression. Tokens are separated
  * by whitespace. An empty tree returns a zero length string. Utilizes
  * helper method postOrder().
  *
  * @return postfix expression of type String
  */
 public String postfix() {
     return postOrder(root);
 }
 private String postOrder(ExpressionTreeNode node) {
     if(node == null) return "";
     return postOrder(node.left) + " " + postOrder(node.right) + " " + node.el;
 }

 /**
  * Returns a prefix representation of the expression. Tokens are separated
  * by whitespace. An empty tree returns a zero length string. Utilizes
  * helper method preOrder().
  *
  * @return String
  */
 public String prefix() {
     return preOrder(root);
 }

 private String preOrder(ExpressionTreeNode node) {
     if(node == null) return "";
     return node.el + " " + preOrder(node.left) + " " + preOrder(node.right);
 }
 /**
  * Simplifies this tree to a specified height h (h >= 0). After
  * simplification, the tree has a height of h. Any subtree rooted
  * at a height of h is replaced by a leaf node containing the
  * evaluated result of that subtree.
  * If the height of the tree is already less than the specified
  * height, the tree is unaffected. *
  * @param h The height to simplify the tree to. */
 public void simplify(int h) {
     removeOverHeight(root, h);

 }
 private void removeOverHeight(ExpressionTreeNode node, int h) {
     if(node == null) {
         // Nothing to remove
         return;
     }
     if(h == 0) {
         // This should now effectively be a root node.
         int val = evaluate(node);
         node.el = String.valueOf(val);
         node.left = null;
         node.right = null;
     } else {
         removeOverHeight(node.left, h - 1);
         removeOverHeight(node.right, h - 1);
     }
 }
 public static void main(String args[]) {
     ExpressionTree test = new ExpressionTree();
     try {
         test.parse("2 + 6 / 3");
         System.out.println(test.evaluate());
     } catch (ParseException e) {
         e.printStackTrace();
     }
 }

}
