import nltk

final_list = []
list_part = []
with open("korpus1.txt") as f:
	for line in f:
		text = nltk.word_tokenize(line)
		text2 = nltk.pos_tag(text)
		#print(text2)
		compare_string = nltk.word_tokenize("make dog") #ugly but works for now, will look into it more
		cp = nltk.pos_tag(compare_string)

		for w in range(len(text2)):
			if text2[w][1] is cp[0][1]:
				list_part.append(text2[w])
			elif text2[w][1] is cp[1][1]:
				list_part.append(text2[w])
		if len(list_part)!=0:
			final_list.append(list_part)
			list_part = []
		
				
print(final_list) #now i print it, do with it as you like. the form is [[(word, type),..,(word, type)],[(word, type),..,(word, type)]] where each sublist is a line from the corpus. if you only want the words add text2[w][0] where it now says list_part.append(text2[w])
