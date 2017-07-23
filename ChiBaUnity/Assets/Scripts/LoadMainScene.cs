using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

/**
 * \author   Jhomar Besmens
 * \version 1.0
 * \date    2017-07-22
 *
 * \brief   This class loads the transition from the Intro_Scene to the Main_Scene.   
 */
public class LoadMainScene : MonoBehaviour
{
    /**
     * This method takes a string and pass it into LoadScene() from the SceneManager.
     * \param[in]   sceneName   The name of the Scene
     */
    public void SwitchToMainScene(string sceneName)
    { 
        SceneManager.LoadScene(sceneName);
    }
    
}