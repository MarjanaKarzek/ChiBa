using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/**
 * \author   Jhomar Besmens
 * \version 1.0
 * \date    2017-07-19
 *
 * \brief   This class quits the unity application.
 */
public class CloseChibaApp : MonoBehaviour {

    /**
     * This method calls the unity method Quit() and has a DebugLog for troubleshooting because Quit() is ignored in the editor.
     */
	public void CloseApp()
    {
        Debug.Log("CloseApp"); // Debug for Play-Mode in Editor
        Application.Quit();
    }
}
