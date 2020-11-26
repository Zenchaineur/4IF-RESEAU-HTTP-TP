import sys

print("Python script")
if len(sys.argv) != 2:
    print("Invalid arguments : script.py <number>")
    sys.exit(1)
arg = sys.argv[1]

print("Factoriel de " + arg)
summ = 1
for i in range(2, int(arg) + 1):
    summ = summ * i

print("Resultat : " + str(summ))
sys.exit(0)
