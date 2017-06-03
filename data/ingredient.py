with open('INGREDIENT.csv', 'r') as f:
	with open('INGREDIENT2.csv', 'w+') as f2:
		while True:
			data = f.readline()
			if not data:
				break
				
			data = data.split(',')
			for i in range(len(data)):
				data[i] = data[i].replace(",", "")

			for i in range(1, len(data)):
				f2.write(str(data[0]) + "," + data[i] + "\n")

