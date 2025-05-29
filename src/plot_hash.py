import numpy as np
import matplotlib.pyplot as plt
def append_to_array(array, value):
    return np.append(array, value)

def parse_file(file_path):
    data = []
    with open(file_path, 'r') as file:
        for line in file:
            splitLine = line.split()
            numbers = list(map(float, splitLine))
            if len(numbers) == 2:
                data.append(numbers)
    return data

def aggregate_and_compute_stats(data):
    # Create a dictionary to aggregate first values based on the third value
    aggregation = {}
    
    for entry in data:
        second, first = entry
        if second not in aggregation:
            aggregation[second] = []
        aggregation[second].append(first)
    
    # Compute average and standard deviation for each unique third value
    stats = {}
    for key, values in aggregation.items():
        avg = np.mean(values)
        std_dev = np.std(values)
        stats[key] = (avg, std_dev)
    
    return stats

def main(file_path):
    data = parse_file(file_path)
    stats = aggregate_and_compute_stats(data)
    xls = np.array([])
    yls = np.array([])
    ydv = np.array([])
    for key, (avg, std_dev) in stats.items():
        xls = append_to_array(xls, key)
        yls = append_to_array(yls, avg)
        ydv = append_to_array(ydv, std_dev)
    plt.plot(xls, yls)
    plt.fill_between(xls, yls - ydv, yls + ydv, alpha=0.2)
    plt.title('Number of "hash value data points" to uniquely identify universal hash')
    plt.xlabel('Prime number')
    plt.ylabel('Data point count')
    plt.show()
main("target_collisions.out")