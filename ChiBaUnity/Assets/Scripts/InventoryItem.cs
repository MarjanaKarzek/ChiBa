using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class InventoryItem : ScriptableObject {

    public string itemName;
    public Sprite sprite;
    public AudioClip picSound;
    public GameObject prefab;
}
