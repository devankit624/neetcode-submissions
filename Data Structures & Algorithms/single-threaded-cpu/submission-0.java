

class Solution {
    public int[] getOrder(int[][] tasks) {
        int n = tasks.length;
        int[] result = new int[n];
        
        // 1. Create an extended array to store: [enqueueTime, processingTime, originalIndex]
        int[][] extendedTasks = new int[n][3];
        for (int i = 0; i < n; i++) {
            extendedTasks[i][0] = tasks[i][0]; // enqueueTime
            extendedTasks[i][1] = tasks[i][1]; // processingTime
            extendedTasks[i][2] = i;           // original index
        }
        
        // 2. Sort tasks by their enqueue time
        Arrays.sort(extendedTasks, (a, b) -> Integer.compare(a[0], b[0]));
        
        // 3. Min-Heap for tasks currently available to the CPU
        // Rules: Sort by processingTime ascending; if tied, sort by originalIndex ascending
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> {
            if (a[1] != b[1]) {
                return Integer.compare(a[1], b[1]);
            }
            return Integer.compare(a[2], b[2]);
        });
        
        long currentTime = 0; // Using long to avoid integer overflow
        int taskIndex = 0;    // Pointer for moving through extendedTasks
        int resIndex = 0;     // Pointer for filling the result array
        
        // 4. Process all tasks
        while (resIndex < n) {
            // If heap is empty and next task hasn't arrived yet, fast-forward time to its arrival
            if (minHeap.isEmpty() && currentTime < extendedTasks[taskIndex][0]) {
                currentTime = extendedTasks[taskIndex][0];
            }
            
            // Push all tasks that have arrived by 'currentTime' into the min-heap
            while (taskIndex < n && extendedTasks[taskIndex][0] <= currentTime) {
                minHeap.offer(extendedTasks[taskIndex]);
                taskIndex++;
            }
            
            // CPU picks the top task from the heap
            int[] currentTask = minHeap.poll();
            result[resIndex++] = currentTask[2]; // Add its original index to the result
            currentTime += currentTask[1];       // Advance timeline by its processing time
        }
        
        return result;
    }
}
