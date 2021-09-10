import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

@FixMethodOrder(NAME_ASCENDING)
public class LinkedListTest {

    private static final ScoringTestRule SCORING_TEST_RULE = new ScoringTestRule();

    @Rule
    public ScoringTestRule scoringTestRule = SCORING_TEST_RULE;

    @Rule
    public Timeout globalTimeOut = Timeout.seconds(15);


    @AfterClass
    public static void printScore() {
        System.out.println();
        System.out.println("LinkedList: " + SCORING_TEST_RULE.getPoints() + " /" + SCORING_TEST_RULE.getTotal() + " points");
        System.out.println("Note that 3 points are NOT included in these tests:");
        System.out.println("3 points for not using an array to sort");
        System.out.println();
    }

    @Test
    public void TestAccidentalJavaLink() throws Exception {
        // Only the Java STL ArrayList should every implement Collection!
        List<String> studentList = new LinkedList<>();
        assertFalse("Test suite is linked to the Java library ArrayList", studentList instanceof java.util.Collection);
    }

    @Test
    @SuppressWarnings("all")    // studentList instanceof List always true suppressed
    public void TestImplementsList() throws Exception {
        LinkedList<String> studentList = new LinkedList<>();
        assertTrue("ArrayList doesn't implement List", studentList instanceof List);
    }

    @Test
    public void TestIndexOfUsingEquals() throws Exception {
        LinkedList<String> arr = new LinkedList<String>();
        String s1 = "horse";
        String s2 = new String("horse");
        arr.add(s1);

        assertEquals("Check to see if they are using == in indexOf", arr.indexOf(s1), arr.indexOf(s2));
    }

    @Test
    public void TestLastIndexOfUsingEquals() throws Exception {
        LinkedList<String> arr = new LinkedList<String>();
        String s1 = "horse";
        String s2 = new String("horse");
        arr.add(s1);

        assertEquals("Check to see if they are using == in lastIndexOf", arr.lastIndexOf(s1), arr.lastIndexOf(s2));
    }

    @Test
    public void TestContainsUsingEquals() throws Exception {
        LinkedList<String> arr = new LinkedList<String>();
        String s1 = "horse";
        String s2 = new String("horse");
        arr.add(s1);

        assertEquals("Check to see if they are using == in contains", arr.contains(s1), arr.contains(s2));
    }

    @Test
    @WorthPoints(points = 1)
    public void addObjectReturnTrueTest() {
        List<String> arr = new LinkedList<String>();
        for (int i = 0; i < 18; i++)
            assertTrue(arr.add("cat" + i));
    }

    @Test
    @WorthPoints(points = 1)
    public void addNullReturnFalseTest() {
        List<String> arr = new LinkedList<String>();
        assertFalse(arr.add(null));
        arr.add("cat");
        assertFalse(arr.add(null));
    }

    @Test
    @WorthPoints(points = 2)
    public void addObjectCorrectlyTest() {
        List<String> arr = new LinkedList<String>();
        for (int i = 0; i < 18; i++)
            assertTrue(arr.add("cat" + i));
        assertEquals(arr.get(17), "cat17");
        assertEquals(arr.get(10), "cat10");
        assertEquals(arr.get(5), "cat5");
    }

    @Test
    @WorthPoints(points = 1)
    public void addIndexOutOfBoundsTest() { //depends on add()
        List<String> arr = new LinkedList<String>();
        assertFalse(arr.add(0, "fish"));
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertFalse(arr.add(-1, "fish"));
        assertFalse(arr.add(24, "fish"));
        assertFalse(arr.add(3, "fish"));
        assertFalse(arr.add(4, "fish"));
    }

    @Test
    @WorthPoints(points = 2)
    public void addAtBeginningTest() { //depends on add()
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertTrue(arr.add(0, "fish"));
        assertEquals(arr.get(0), "fish");
        assertTrue(arr.add(0, "lizard"));
        assertEquals(arr.get(0), "lizard");
        assertEquals(arr.get(1), "fish");
    }

    @Test
    @WorthPoints(points = 2)
    public void addAtMiddleAndEndTest() {//depends on get, add
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertTrue(arr.add(1, "fish"));
        assertEquals(arr.get(1), "fish");
        assertTrue(arr.add(3, "lizard"));
        assertEquals(arr.get(3), "lizard");
        assertEquals(arr.get(4), "snake");
    }

    @Test
    @WorthPoints(points = 1)
    public void addIndexNullReturnFalseTest() {//depends on add()
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertFalse(arr.add(0, null));
        assertFalse(arr.add(1, null));
    }

    @Test
    @WorthPoints(points = 1)
    public void clearNonEmptySetTest() { //depends on add, get
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        arr.clear();
        assertNull(arr.get(0));
        arr.add("fish");
        arr.clear();
        assertNull(arr.get(0));
    }

    @Test
    @WorthPoints(points = 1)
    public void clearEmptySetTest() { //requires get
        List<String> arr = new LinkedList<String>();
        arr.clear();
        assertNull(arr.get(0));
    }

    @Test
    @WorthPoints(points = 1)
    public void containsNullReturnFalseTest() //requires add
    {
        List<String> arr = new LinkedList<String>();
        assertFalse(arr.contains(null));
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertFalse(arr.contains(null));
    }

    @Test
    @WorthPoints(points = 2)
    public void containsNonnullReturnTest()//requires add
    {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertTrue(arr.contains("dog"));
        assertTrue(arr.contains("cat"));
        assertTrue(arr.contains("snake"));
        assertFalse(arr.contains("fish"));
        assertFalse(arr.contains("lizard"));
    }

    @Test
    @WorthPoints(points = 1)
    public void getOutOfBoundsTest()//requires add
    {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertNull(arr.get(-1));
        assertNull(arr.get(24));
        assertNull(arr.get(3));
    }

    @Test
    @WorthPoints(points = 2)
    public void getInBoundsTest()//requires add
    {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertEquals(arr.get(0), "cat");
        assertEquals(arr.get(1), "dog");
        assertEquals(arr.get(2), "snake");
    }

    @Test
    @WorthPoints(points = 1)
    public void indexOfNotPresentTest()//requires add
    {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertEquals(arr.indexOf("horse"), -1);
        assertEquals(arr.indexOf("fish"), -1);
        assertEquals(arr.indexOf(null), -1);
    }

    @Test
    @WorthPoints(points = 2)
    public void indexOfPresentTest()//requires add
    {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        arr.add("cat");
        assertEquals(arr.indexOf("cat"), 0);
        assertEquals(arr.indexOf("snake"), 2);
        assertEquals(arr.indexOf("dog"), 1);
    }

    @Test
    @WorthPoints(points = 1)
    public void emptyTest()//requires add
    {
        List<String> arr = new LinkedList<String>();
        assertTrue(arr.isEmpty());
        arr.add("cat");
        assertFalse(arr.isEmpty());
    }

    @Test
    @WorthPoints(points = 1)
    public void lastindexOfNotPresent()//requires add
    {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertEquals(arr.lastIndexOf("horse"), -1);
        assertEquals(arr.lastIndexOf("fish"), -1);
        assertEquals(arr.lastIndexOf(null), -1);
    }

    @Test
    @WorthPoints(points = 1)
    public void lastindexOfPresentTest()//requires add
    {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        arr.add("cat");
        assertEquals(arr.indexOf("cat"), 0);
        assertEquals(arr.indexOf("snake"), 2);
        assertEquals(arr.indexOf("dog"), 1);
    }

    @Test
    @WorthPoints(points = 2)
    public void lastIndexOfDuplicatesTest()//requires add
    {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        arr.add("cat");
        arr.add("dog");
        assertEquals(arr.lastIndexOf("cat"), 3);
        assertEquals(arr.lastIndexOf("snake"), 2);
        assertEquals(arr.lastIndexOf("dog"), 4);
    }

    @Test
    @WorthPoints(points = 1)
    public void setOutOfBoundsTest() {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertNull(arr.set(-1, "fish"));
        assertNull(arr.set(3, "fish"));
        assertNull(arr.set(0, null));

    }

    @Test
    @WorthPoints(points = 2)
    public void setInBoundsTest()//requires add, get
    {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertEquals(arr.set(0, "fish"), "cat");
        assertEquals(arr.get(0), "fish");
        assertEquals(arr.set(1, "horse"), "dog");
        assertEquals(arr.get(1), "horse");
        assertEquals(arr.set(2, "tiger"), "snake");
        assertEquals(arr.get(2), "tiger");

    }

    @Test
    @WorthPoints(points = 1)
    public void sizeTest()//requires add, clear
    {
        List<String> arr = new LinkedList<String>();
        assertEquals(arr.size(), 0);
        arr.add("cat");
        assertEquals(arr.size(), 1);
        arr.add("dog");
        assertEquals(arr.size(), 2);
        arr.clear();
        assertEquals(arr.size(), 0);
    }

    @Test
    @WorthPoints(points = 1)
    public void forwardSortTest()//requires add, get
    {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        arr.add("tiger");
        arr.add("elephant");
        arr.add("dragon");
        arr.add("horse");
        arr.add("snake");
        arr.sort(true);
        assertEquals(arr.get(0), "cat");
        assertEquals(arr.get(1), "dog");
        assertEquals(arr.get(2), "dragon");
        assertEquals(arr.get(3), "elephant");
        assertEquals(arr.get(4), "horse");
        assertEquals(arr.get(5), "snake");
        assertEquals(arr.get(6), "snake");
        assertEquals(arr.get(7), "tiger");
    }

    @Test
    @WorthPoints(points = 1)
    public void backwardSortTest()//requires add, get
    {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        arr.add("tiger");
        arr.add("elephant");
        arr.add("dragon");
        arr.add("horse");
        arr.add("snake");
        arr.sort(false);
        assertEquals(arr.get(7), "cat");
        assertEquals(arr.get(6), "dog");
        assertEquals(arr.get(5), "dragon");
        assertEquals(arr.get(4), "elephant");
        assertEquals(arr.get(3), "horse");
        assertEquals(arr.get(2), "snake");
        assertEquals(arr.get(1), "snake");
        assertEquals(arr.get(0), "tiger");
    }

    @Test
    @WorthPoints(points = 1)
    public void sortBackwardFromForwardTest()//requires add, get
    {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("dragon");
        arr.add("elephant");
        arr.add("horse");
        arr.add("snake");
        arr.add("snake");
        arr.add("tiger");
        arr.sort(false);
        assertEquals(arr.get(7), "cat");
        assertEquals(arr.get(6), "dog");
        assertEquals(arr.get(5), "dragon");
        assertEquals(arr.get(4), "elephant");
        assertEquals(arr.get(3), "horse");
        assertEquals(arr.get(2), "snake");
        assertEquals(arr.get(1), "snake");
        assertEquals(arr.get(0), "tiger");
    }

    @Test
    @WorthPoints(points = 1)
    public void sortForwardFromBackwardTest()//requires add, get
    {
        List<String> arr = new LinkedList<String>();
        arr.add("tiger");
        arr.add("snake");
        arr.add("snake");
        arr.add("horse");
        arr.add("elephant");
        arr.add("dragon");
        arr.add("dog");
        arr.add("cat");
        arr.sort(true);
        assertEquals(arr.get(0), "cat");
        assertEquals(arr.get(1), "dog");
        assertEquals(arr.get(2), "dragon");
        assertEquals(arr.get(3), "elephant");
        assertEquals(arr.get(4), "horse");
        assertEquals(arr.get(5), "snake");
        assertEquals(arr.get(6), "snake");
        assertEquals(arr.get(7), "tiger");
    }

    @Test
    @WorthPoints(points = 1)
    public void removeObjectNotPresentTest() {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertFalse(arr.remove("lion"));
        assertFalse(arr.remove("leopard"));
        assertFalse(arr.remove("wolf"));
    }

    @Test
    @WorthPoints(points = 1)
    public void removeObjectReturnTrueTest() {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertTrue(arr.remove("cat"));
        assertTrue(arr.remove("snake"));
        assertTrue(arr.remove("dog"));
    }

    @Test
    @WorthPoints(points = 1)
    public void removeObjectRemovesTest() {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        arr.remove("cat");
        assertEquals(arr.get(0), "dog");
        arr.remove("dog");
        assertEquals(arr.get(0), "snake");
        arr.remove("snake");
        assertNull(arr.get(0));
    }

    @Test
    @WorthPoints(points = 1)
    public void removeObjectRemovesTest2() {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        arr.add("snake");
        arr.add("horse");
        arr.add("tiger");
        arr.remove("tiger");
        assertNull(arr.get(5));
        arr.remove("dog");
        assertEquals(arr.get(1), "snake");
        assertEquals(arr.get(2), "snake");

    }

    @Test
    @WorthPoints(points = 1)
    public void removeIndexOutOfBoundsTest() {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertNull(arr.remove(3));
        assertNull(arr.remove(-1));
        assertNull(arr.remove(12));
    }

    @Test
    @WorthPoints(points = 1)
    public void removeIndexReturnsObjectTest() {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        assertEquals(arr.remove(0), "cat");
        assertEquals(arr.remove(1), "snake");
        assertEquals(arr.remove(0), "dog");
    }

    @Test
    @WorthPoints(points = 1)
    public void removeIndexRemovesObjectTest() {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        arr.remove(0);
        assertEquals(arr.get(0), "dog");
        arr.remove(0);
        assertEquals(arr.get(0), "snake");
        arr.remove(0);
        assertNull(arr.get(0));
    }

    @Test
    @WorthPoints(points = 1)
    public void removeIndexRemovesObjectTest2() {
        List<String> arr = new LinkedList<String>();
        arr.add("cat");
        arr.add("dog");
        arr.add("snake");
        arr.add("snake");
        arr.add("horse");
        arr.add("tiger");
        arr.remove(5);
        assertNull(arr.get(5));
        arr.remove(1);
        assertEquals(arr.get(1), "snake");
        assertEquals(arr.get(2), "snake");

    }

}

