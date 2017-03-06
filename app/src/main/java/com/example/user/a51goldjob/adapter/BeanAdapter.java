package com.example.user.a51goldjob.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;

import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.chyjr.goldjob.fr.utils.BeanUtils;

import java.util.List;

public class BeanAdapter extends BaseAdapter
{
  private int[] mTo;
  private String[] mFrom;
  private ViewBinder mViewBinder;
  private List<?> mData;
  private int mResource;
  private LayoutInflater mInflater;


  public BeanAdapter(Context context, List<?> data, int resource, String[] from, int[] to)
  {
    this.mData = data;
    this.mResource = resource;
    this.mFrom = from;
    this.mTo = to;
    this.mInflater = ((LayoutInflater)context.getSystemService("layout_inflater"));


  }

  public int getCount() {
    return this.mData.size();
  }

  public Object getItem(int position)
  {
    return this.mData.get(position);
  }

  public long getItemId(int position)
  {
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent)
  {
    return createViewFromResource(position, convertView, parent, this.mResource);
  }

  private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource)
  {
    View v;
    if (convertView == null)
      v = this.mInflater.inflate(resource, parent, false);
    else {
      v = convertView;
    }

    bindView(position, v);

    return v;
  }

  protected void bindView(int position, View view)
  {
//	if(index!=1000000000&&index==position){
//		ImageView img=(ImageView)view.findViewById(R.id.itemSelectedImg);
//		if(img!=null){
//			img.setVisibility(View.VISIBLE);
//		}
//	}  
    Object dataSet = this.mData.get(position);
    if (dataSet == null) {
      return;
    }

    ViewBinder binder = this.mViewBinder;
    String[] from = this.mFrom;
    int[] to = this.mTo;
    int count = to.length;

    for (int i = 0; i < count; ++i) {
      View v = view.findViewById(to[i]);
      if (v != null) {
        Object data = getPropertyValue(dataSet, from[i]);
        v.setTag(Integer.valueOf(position));
        String text = (data == null) ? "" : data.toString();
        if (text == null) {
          text = "";
        }

        boolean bound = false;
        if (binder != null) {
          bound = binder.setViewValue(v, data, text);
        }

        if (!(bound)) {
          if (v instanceof Checkable) {
            if (data instanceof Boolean) {
              ((Checkable)v).setChecked(((Boolean)data).booleanValue());
              continue; } if (v instanceof TextView)
            {
              setViewText((TextView)v, text);
              continue; }
            throw new IllegalStateException(v.getClass().getName() + 
              " should be bound to a Boolean, not a " + 
              data.getClass());
          }
          if (v instanceof TextView)
          {
            setViewText((TextView)v, text);
          } else if (v instanceof ImageView)
            if (data instanceof Integer)
              setViewImage((ImageView)v, ((Integer)data).intValue());
            else
              setViewImage((ImageView)v, text);

          else
            throw new IllegalStateException(v.getClass().getName() + " is not a " + 
              " view that can be bounds by this SimpleAdapter");
        }
      }
    }
  }

  private Object getPropertyValue(Object dataSet, String name)
  {
    return BeanUtils.getPropertyValue(dataSet, name);
  }

  public ViewBinder getViewBinder()
  {
    return this.mViewBinder;
  }

  public void setViewBinder(ViewBinder viewBinder)
  {
    this.mViewBinder = viewBinder;
  }

  public void setViewImage(ImageView v, int value)
  {
    v.setImageResource(value);
  }

  public void setViewImage(ImageView v, String value)
  {
    try
    {
      v.setImageResource(Integer.parseInt(value));
    } catch (NumberFormatException nfe) {
      v.setImageURI(Uri.parse(value));
    }
  }

  public void setViewText(TextView v, String text)
  {
    v.setText(text);
  }
}