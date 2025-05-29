import numpy as np
import matplotlib.pyplot as plt
def append_to_array(array, value):
    return np.append(array, value)

def parse_file(file_path):
    data = []
    with open(file_path, 'r') as file:
        for line in file:
            splitLine = line.split()
            del splitLine[1]
            numbers = list(map(float, splitLine))
            if len(numbers) == 4:
                data.append(numbers)
    return data

def aggregate_and_compute_stats(data):
    # Create a dictionary to aggregate first values based on the third value
    aggregation = {}
    
    for entry in data:
        first, second, third, fourth = entry
        if third not in aggregation:
            aggregation[third] = []
        aggregation[third].append(first)
    
    # Compute average and standard deviation for each unique third value
    stats = {}
    for key, values in aggregation.items():
        avg = np.mean(values)
        std_dev = np.std(values)
        stats[key] = (avg, std_dev)
    
    return stats

def main(file_path, title, savepath):
    plt.clf()
    data = parse_file(file_path)
    stats = aggregate_and_compute_stats(data)
    xls = np.array([])
    yls = np.array([])
    ydv = np.array([])
    for key, (avg, std_dev) in stats.items():
        xls = append_to_array(xls, key)
        yls = append_to_array(yls, avg)
        ydv = append_to_array(ydv, std_dev)

    plt.ylim(0, 10000000)
    plt.plot(xls, yls)
    plt.fill_between(xls, yls - ydv, yls + ydv, alpha=0.2)
    plt.title(title)
    plt.xlabel('Number of buckets')
    plt.ylabel('Operations to break')
    plt.savefig(savepath)

# Breaking hash function CMS
main("exp1_false.out", 'CMS-attack-2, R = log2(B), Universal Hashing', 'breaking_universal.png')
# Breaking random oracle CMS
main("exp1_true.out",  'CMS-attack-2, R = log2(B), Random Oracle Model', 'breaking_random_oracle.png')
# Additive attack of CMS with hash functions
main("exp2_false.out", 'CMS-attack-1, R = log2(B), Universal Hashing', 'additive_hash.png')
# Additive attack of CMS with random oracle
main("exp2_true.out",  'CMS-attack-1, R = log2(B), Random Oracle Model', 'additive_oracle.png')