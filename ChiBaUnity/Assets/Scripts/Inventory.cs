using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

/**
 * \author Carsten Seifert
 * \date    2017-07-19
 * \brief   This class sets up the inventory. Based on the book: Spiele entwickeln mit Unity 5 
 */
public class Inventory : MonoBehaviour {

    public Image[] imageItemPreview;    /*!< Variable for the Image array */
    public Dictionary<InventoryItem, int> items = new Dictionary<InventoryItem, int>(); /*!< Variable for the Dictionary */

    /**
     * This method calls UpdateView() when the script is instantiate
     */
    private void Start()
    {
        UpdateView();
    }

    /**
     * This method gets the imageItemPreview array and sets enabled to false. For each KeyValuePair in items it sets show the sprite and enable it. 
     */
    void UpdateView()
    {
        int imageCount = imageItemPreview.Length;

        for(int i = 0; i<imageCount; i++)
        {
            imageItemPreview[i].enabled = false;
        }

        int index = 0;
        foreach(KeyValuePair<InventoryItem, int> current in items)
        {
            imageItemPreview[index].enabled = true;
            imageItemPreview[index].sprite = current.Key.sprite;
            index++;
        }
    }

    /**
     * This method checks if item contains already the key id and if not it adds the item to the inventory.
     */
    public bool AddItem(InventoryItem id)
    {
        if (!items.ContainsKey(id))
        {
            if (items.Count < imageItemPreview.Length)
                items.Add(id, 1);
            else
                return false;
        }
        else
        {
            items[id]++;
        }
        UpdateView();
        return true;
    }

    /**
     * This method checks if item contains the correct key id and if yes it removes the item from the inventory.
     */
    public bool RemoveItem(InventoryItem id)
    {
        if (items.ContainsKey(id))
        {
            if (items[id] > 0)
                items.Remove(id);

            UpdateView();
            return true;
        }
        return false;
    }
    


}
