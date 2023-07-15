import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, Button, ImageBackground, I18nManager, TouchableOpacity } from 'react-native';

export default function App() {
  const [selectedGame, setSelectedGame] = useState('');
  const [gameSaveNames, setGameSaveNames] = useState([]);
  const [scoreBoard, setScoreBoard] = useState([]);
  const ip='192.168.43.220';         //Replace it with your pc's IP address

  useEffect(() => {
    fetchGameNames();
  }, []);

  const fetchGameNames = async () => {
    try {
      const response = await fetch('http://'+ip+':8082/servlet_prj/GameSaveDB?action=getAllGameSaveNames&name');
      const responseData = await response.text(); // Parse the response as text
      const gameNames = responseData.split('\n').filter(name => name.trim() !== ''); // Split and filter empty lines
      setGameSaveNames(gameNames);
    } catch (error) {
      console.error('Error:', error);
    }
  };


  useEffect(() => {
    if (selectedGame !== '') {
      fetchScoreTable(selectedGame);
    }
  }, [selectedGame]);


  const fetchScoreTable = async (selectedGame) => {
    try {
      const response = await fetch('http://'+ip+':8082/servlet_prj/GameSaveDB?action=getScoreBoard&name='+selectedGame);
      const responseData = await response.text(); // Parse the response as text

      // Extract player names and scores from the response string
      const scoreTable = responseData
        .replace(/[{}]/g, '')            // Remove curly braces
        .split(', ')                     // Split by comma and space
        .map(pair => pair.split('='))    // Split each pair into name and score
        .reduce((table, [name, score]) => {
          table.push({ name, score: parseInt(score) });
          return table;
        }, []);

      setScoreBoard(scoreTable); // Set the scoreBoard state with the extracted scoreTable
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handleGamePress = (gameName) => {
    setSelectedGame(gameName);
  };

  const renderScoreTable = () => {
    if (!scoreBoard || scoreBoard.length === 0) {
      return null;
    }

    const LayoutRTL = () => (
      <>
        <Text style={[styles.headerText, styles.scoreHeaderText]}>Score</Text>
        <Text style={[styles.headerText, styles.nameHeaderText]}>Player</Text>
      </>
    );

    const LayoutLTR = () => (
      <>
        <Text style={[styles.headerText, styles.nameHeaderText]}>Player</Text>
        <Text style={[styles.headerText, styles.scoreHeaderText]}>Score</Text>
      </>
    );

    const HeaderLayout = I18nManager.isRTL ? LayoutRTL : LayoutLTR;

    return (
      <View style={styles.scoreTable}>
        <View style={styles.tableHeader}>
          <HeaderLayout />
        </View>
        {scoreBoard.map((player, index) => (
          <View key={index} style={styles.scoreRow}>
            <View style={styles.nameCell}>
              <Text style={styles.nameText}>{player.name}</Text>
            </View>
            <View style={styles.scoreCell}>
              <Text style={styles.scoreText}>{player.score}</Text>
            </View>
          </View>
        ))}
      </View>
    );
  };

  return (
    <ImageBackground source={require('./scrabble.png')} style={styles.backgroundImage}>
      <View style={styles.container}>
        <Text style={styles.title}>Scrabble</Text>
        <Text style={styles.subtitle}>Game Saves</Text>
        {gameSaveNames.map((gameName, index) => (
          <View key={index}>
            <TouchableOpacity style={styles.button} onPress={() => handleGamePress(gameName)}>
              <Text style={styles.buttonText}>{gameName}</Text>
            </TouchableOpacity>
            {index !== gameSaveNames.length - 1 && <View style={styles.separator} />}
          </View>
        ))}


        {selectedGame !== '' && (
          <View style={styles.selectedGameContainer}>
            <Text style={styles.selectedGameText}>Selected Game: {selectedGame}</Text>
            {renderScoreTable()}
          </View>
        )}
      </View>
    </ImageBackground>
  );
}

const styles = StyleSheet.create({
  button: {
    backgroundColor: 'rgb(33, 150, 243)', // Red color
    padding: 10,
    borderRadius: 5,
    marginBottom: 10,
  },
  buttonText: {
    fontSize: 16,
    fontWeight: 'bold',
    textTransform: 'none', // Prevent text capitalization
    color: '#fff', // Set text color to white
  },

  backgroundImage: {
    flex: 1,
    resizeMode: 'cover',
    justifyContent: 'center',
  },
  container: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)', // Add an optional overlay for better visibility
    alignItems: 'center',
    justifyContent: 'center',
    padding: 16,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 8,
    color: '#fff', // Set the text color to white
    textAlign: 'left', // Align the title to the left
  },
  subtitle: {
    fontSize: 18,
    marginBottom: 16,
    color: '#fff', // Set the text color to white
    textAlign: 'left', // Align the subtitle to the left
  },
  separator: {
    height: 16, // Adjust this value as needed
  },
  selectedGameContainer: {
    marginTop: 16,
    alignItems: 'center',
  },
  selectedGameText: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 8,
    color: '#fff', // Set the text color to white
    textAlign: 'left', // Align the selected game text to the left
  },
  scoreTable: {
    marginTop: 16,
    borderWidth: 1,
    borderColor: '#fff',
    borderRadius: 4,
    padding: 8,
    minWidth: 300, // Set the minimum width for the table
  },
  tableHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 8,
  },
  headerText: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#fff', // Set the text color to white
  },
  nameHeaderText: {
    flex: 1,
    textAlign: I18nManager.isRTL ? 'right' : 'left',
  },
  scoreHeaderText: {
    flex: 1,
    textAlign: I18nManager.isRTL ? 'left' : 'right',
  },
  scoreRow: {
    flexDirection: I18nManager.isRTL ? 'row-reverse' : 'row',
    alignItems: 'center',
    marginBottom: 8,
    borderBottomWidth: 1,
    borderBottomColor: '#fff',
    paddingBottom: 4,
  },
  scoreCell: {
    flex: 1,
    alignItems: I18nManager.isRTL ? 'flex-start' : 'flex-end',
  },
  nameCell: {
    flex: 1,
    alignItems: I18nManager.isRTL ? 'flex-end' : 'flex-start',
  },
  scoreText: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#fff',
    textAlign: I18nManager.isRTL ? 'left' : 'right',
  },
  nameText: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#fff',
    textAlign: I18nManager.isRTL ? 'right' : 'left',
  },
});
