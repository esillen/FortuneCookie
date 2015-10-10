import nltk
import en

final_list = []
list_part = []
test = []
with open("testKorpus.txt") as f:
	for line in f:
		text = nltk.word_tokenize(line)
		text2 = nltk.pos_tag(text)
		#print(text2)
		compare_string = nltk.word_tokenize("make dog Twas made raths outgrabe he asks") #ugly but works for now, will look into it more: VB, NN, NNP, VBD, NNS, VBP
		cp = nltk.pos_tag(compare_string)
		
		#print en.noun.singular("cows")
		for w in range(len(text2)):
			if text2[w][1] is cp[0][1]:
				list_part.append(text2[w])
			elif text2[w][1] is cp[1][1]:
				list_part.append(text2[w])
			elif text2[w][1] is cp[2][1]:
				text2[w][1] = cp[1][1]
				list_part.append(text2[w])
			elif text2[w][1] is cp[3][1]:
				temp = str(text2[w][0])
				temp  = en.verb.present(temp)
				test = [temp, cp[0][1]]
				list_part.append(tuple(test))
			elif text2[w][1] is cp[5][1]:
				test = [text2[w][0], cp[0][1]]
				#text2[w][1] = cp[0][1]	
				list_part.append(tuple(test))
			elif text2[w][1] is cp[4][1]:
				temp = str(text2[w][0])
				temp = en.noun.singular(str((text2[w][0])))
				test = [temp, cp[1][1]]
				#text2[w][1] = tuple(cp[1][1])
				list_part.append(tuple(test))
			elif text2[w][1] is cp[7][1]:
				temp = str(text2[w][0])
				temp  = en.verb.present(temp)
				test = [temp, cp[0][1]]
				list_part.append(tuple(test))
				
		if len(list_part)!=0:
			final_list.append(list_part)
			list_part = []
			test = []
index = 0
#print cp

for i in final_list:			
	print(final_list[index])
	index = index + 1 #now i print it, do with it as you like. the form is [[(word, type),..,(word, type)],[(word, type),..,(word, type)]] where each sublist is a line from the corpus. if you only want the words add text2[w][0] where it now says list_part.append(text2[w])
