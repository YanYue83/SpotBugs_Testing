package DictionaryCommanLine;

import PrefixTree.Trie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class DictionaryManagement {
    protected Trie trie = new Trie();

    /**
     * Nhap tu thu cong.
     */
    public void insertFromCommandline(Dictionary dictionary) {
        Scanner scan = new Scanner(System.in);
        while (scan.hasNext()) {
            String target = scan.next();
            String explain = scan.next();
            Word word = new Word(target, explain);
            dictionary.add(word);
        }
    }

    /**
     * Nhap tu trong file txt.
     */
    public void insertFromFile(Dictionary dictionary, String path) {
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String englishWord = bufferedReader.readLine();
            englishWord = englishWord.replace("|", "");
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Word word = new Word();
                word.setWord_target(englishWord.trim());
                String meaning = line + "\n";
                while ((line = bufferedReader.readLine()) != null)
                    if (!line.startsWith("|")) meaning = meaning + line + "\n";
                    else {
                        englishWord = line.replace("|", "");
                        break;
                    }
                word.setWord_explain(meaning.trim());
                dictionary.add(word);
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("An error occur with file: " + e);
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
        }
    }


    /**
     * Xuat du lieu ra file.
     */
    public void exportToFile(Dictionary dictionary, String path) {
        try {
            FileWriter fileWriter = new FileWriter(path);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (Word word : dictionary) {
                bufferedWriter.write("|" + word.getWord_target() + "\n" + word.getWord_explain());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    /**
     * Hien thi toan bo tu dien.
     */
    public void showAllWords(Dictionary dictionary) {
        for (Word word : dictionary) {
            System.out.println(word.toString());
        }
    }

    /**
     * Tra cuu tu co trong tu dien khong, tra ve chi so cua tu trong tu dien.
     */
    public int dictionaryLookUp(Dictionary dictionary, String target) {
        try {
            dictionary.sort(new SortbyWord());
            // tim kiem bang thuat toan tim kiem nhi phan
            int l = 0;
            int r = dictionary.size() - 1;
            while (l <= r) {
                int mid = l + (r - l) / 2;
                int result = dictionary.get(mid).getWord_target().compareTo(target);
                if (result == 0) return mid;
                else if (result < 0) l = mid + 1;
                else r = mid - 1;
            }

        } catch (NullPointerException e) {
            System.out.println("NullPointerException when looking up!");
        }
        return -1;
    }

    /**
     * Tra tu lien quan.
     */
    public ObservableList<String> searchWord(Dictionary dictionary, String key) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            List<String> results = trie.autoComplete(key);
            if (results != null) {
                int length = Math.min(results.size(), 15);
                for (int i = 0; i < length; i++) list.add(results.get(i));
            }
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
        }
        return list;
    }

    /**
     * Them tu vao du lieu tu dien.
     */
    public void addWord(Word word, String path) {
        try {
            FileWriter fileWriter = new FileWriter(path, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("|" + word.getWord_target() + "\n" + word.getWord_explain());
            bufferedWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            System.out.println("NullPointerException !");
        }
    }

    /**
     * Sua tu trong tu dien.
     */
    public void updateWord(Dictionary dictionary, int i, String meaning, String path) {
        try {
            dictionary.get(i).setWord_explain(meaning);
            exportToFile(dictionary, path);
        } catch (NullPointerException e) {
            System.out.println("NullPointerException !");
        }
    }

    /**
     * Xoa tu trong tu dien.
     */
    public void deleteWord(Dictionary dictionary, int i, String path) {
        try {
            dictionary.remove(i);
            exportToFile(dictionary, path);
        } catch (NullPointerException e) {
            System.out.println("NullPointerException !");
        }
    }


    public void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }

    /**
     * them tu trong tu dien vao cay tien to.
     */
    public void setTrie(Dictionary dictionary) {
        try {
            for (Word word : dictionary) trie.insert(word.getWord_target());
        } catch (NullPointerException e) {
            System.out.println("NullPointerException for Trie! ");
        }
    }
}
