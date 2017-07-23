using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/**
 * \author   Jhomar Besmens
 * \version 1.0
 * \date    2017-07-22
 *
 * \brief   This class defines scriptable objects for the inventory
 */
public class InventoryItem : ScriptableObject {

    public string itemName; /*!< Variable for the name of the item */ 
    public Sprite sprite;   /*!< Variable for the sprite */
    public GameObject prefab;   /*!< Variable for the prefab */
}
