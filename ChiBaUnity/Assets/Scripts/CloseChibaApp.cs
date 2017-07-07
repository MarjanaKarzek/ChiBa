using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CloseChibaApp : MonoBehaviour {

	public void CloseApp()
    {
        Debug.Log("CloseApp");
        Application.Quit();
    }
}
