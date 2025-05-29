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
      if len(splitLine) == 4:

        data.append((float(splitLine[0]), float(splitLine[2])))
  return data

def aggregate_and_compute_stats(data):
  # Create a dictionary to aggregate first values based on the third value
  aggregation = {}
  
  for entry in data:
    first, second = entry
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

def main(colors, file_paths, labels, title, savepath):
  plt.clf()

  for i, file_path in enumerate(file_paths):
    data = parse_file(file_path)
    stats = aggregate_and_compute_stats(data)
    xls, yls, ydv = np.array([]), np.array([]), np.array([])

    for key, (avg, std_dev) in stats.items():
      xls = append_to_array(xls, key)
      yls = append_to_array(yls, avg)
      ydv = append_to_array(ydv, std_dev)

    # Plot each file's data
    plt.plot(xls, yls, label=labels[i], color=colors[i])
    plt.fill_between(xls, yls - ydv, yls + ydv, alpha=0.2, color=colors[i])

  plt.ylim(0, 9000000)
  plt.title(title, fontsize=16)
  plt.xlabel('Bucket size', fontsize=14)
  plt.ylabel('Number of operations', fontsize=14)
  plt.legend()
  plt.show()
  plt.savefig(savepath)
prop_cycle = plt.rcParams['axes.prop_cycle']
colors = prop_cycle.by_key()['color']

main([colors[0], colors[1]], ["exp1_apache.out", "exp2_apache.out"], ["Value based attack", "Additive attack"], "Apache CMS", "dyn3.pdf")
